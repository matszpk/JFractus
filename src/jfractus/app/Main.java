/**
 * Main.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.io.File;
import java.util.*;
import java.awt.Dimension;
import javax.swing.*;

import org.apache.commons.cli.*;

import jfractus.app.ui.MainWindow;

public class Main
{
	static private Options cliOptions;
	
	private static void createAndShowGUI()
	{
		MainWindow frame = new MainWindow();
		frame.setVisible(true);
	}
	
	private static void createCommandLineOptions()
	{
		cliOptions = new Options();
		cliOptions.addOption("n", "nogui", false, Resources.getString("CLINoGUI"));
		cliOptions.addOption("a", "antialias", true, Resources.getString("CLIAntialias"));
		cliOptions.addOption("A", "sampling-size", true,
				Resources.getString("CLISamplingSize"));
		cliOptions.addOption("L", "libraries", true, Resources.getString("CLILibraries"));
		cliOptions.addOption("S", "image-size", true, Resources.getString("CLIImageSize"));
		cliOptions.addOption("t", "threads", true, Resources.getString("CLIThreads"));
		cliOptions.addOption("p", "progress", false, Resources.getString("CLIProgress"));
		cliOptions.addOption("P", "save-prefs", false, Resources.getString("CLISavePrefs"));
		cliOptions.addOption("h", "help", false, Resources.getString("CLIHelp"));
	}
	
	private static class CMDLineProgressEventListener implements RenderProgressListener
	{
		private static final String renderingProgressText =
			Resources.getString("RenderingProgress");
		private static final String filteringProgressText =
			Resources.getString("FilteringProgress");

		public void updateProgress(RenderProgressEvent e)
        {
			if (e.hasDone())
			{
				System.out.printf(Resources.getString("Finished"));
				return;
			}
			
			String text = null;
			switch(e.getProcess())
			{
			case RENDERING:
				text = renderingProgressText;
				break;
			case FILTERING:
				text = filteringProgressText;
				break;
			}
			
			System.out.printf(text + ": %03.3f%%\r", e.getProgress());
        }
		
	}
	
	private static class CMDLineImageWriteProgressListener
			implements ImageWriterProgressListener
	{
		private static final String writingProgressText =
			Resources.getString("WritingProgress");

        public void updateProgress(ImageWriterProgressEvent e)
        {
	        System.out.printf(writingProgressText + ": %03.3f%%\r", e.getProgress());
        }
		
	}
	
	private static void parseSize(String sizeStr, Dimension dim)
			throws ArgumentParseException, BadValueOfArgumentException
	{
		Scanner scanner = new Scanner(sizeStr);
		scanner.useLocale(Locale.ENGLISH);
		scanner.useDelimiter("x");
		
		int outWidth = 0, outHeight = 0;
		try
		{
			outWidth = scanner.nextInt();
			outHeight = scanner.nextInt();
			
			if (outWidth < 0 || outHeight < 0)
				throw new BadValueOfArgumentException("Bad value of argument");
		}
		catch(InputMismatchException e)
		{ throw new ArgumentParseException("Command line parse exception"); }
		catch(NoSuchElementException e)
		{ throw new ArgumentParseException("Command line parse exception"); }
		
		if(scanner.hasNext())
			throw new ArgumentParseException("Command line parse exception");
		
		dim.setSize(outWidth, outHeight);
	}

	public static void main(String[] args)
	{		
		createCommandLineOptions();
		GnuParser parser = new GnuParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(null);
		
		CommandLine cmdLine = null;
		
		try
		{ cmdLine = parser.parse(cliOptions, args); }
		catch(ParseException e)
		{
			System.err.println(Resources.getString("CLIParseError"));
			return;
		}
		
		String functionsLibPaths = cmdLine.getOptionValue("libraries");
		int threadsNum = FractusPreferencesFactory.prefs.getThreadsNumber();
		Dimension outSize = new Dimension
			(FractusPreferencesFactory.prefs.getDefaultImageWidth(),
			FractusPreferencesFactory.prefs.getDefaultImageHeight());
		AntialiasConfig aaConfig = FractusPreferencesFactory.prefs.
				getDefaultAntialiasConfig();
		boolean printProgress = cmdLine.hasOption("progress");
		
		try
		{
			String threadsNumString = cmdLine.getOptionValue("threads");
			String imageSizeString = cmdLine.getOptionValue("image-size");
			String aaMethodString = cmdLine.getOptionValue("antialias");
			String samplingSizeString = cmdLine.getOptionValue("sampling-size");
			
			if (functionsLibPaths != null)
				FunctionsLoaderFactory.loader.setClassPathsFromString(functionsLibPaths);
			
			if (aaMethodString != null)
			{
				if (aaMethodString.equals("none"))
					aaConfig.setMethod(AntialiasConfig.Method.NONE);
				else if (aaMethodString.equals("normal"))
					aaConfig.setMethod(AntialiasConfig.Method.NORMAL);
				else throw new BadValueOfArgumentException("Bad value of argument");
			}
			if (threadsNumString != null)
				threadsNum = Integer.valueOf(threadsNumString).intValue();
			if (imageSizeString != null)
				parseSize(imageSizeString, outSize);
			if (samplingSizeString != null)
			{
				Dimension samplingSize = new Dimension();
				parseSize(samplingSizeString, samplingSize);
				aaConfig.setSamplingSize(samplingSize.width, samplingSize.height);
			}
			
			if (cmdLine.hasOption("save-prefs"))
			{
				FunctionsLoaderFactory.loader.putClassPathsToPrefs();
				FractusPreferencesFactory.prefs.setThreadsNumber(threadsNum);
				FractusPreferencesFactory.prefs.setDefaultImageSize(outSize.width,
						outSize.height);
				FractusPreferencesFactory.prefs.setDefaultAntialiasConfig(aaConfig);
			}
		}
		catch (ArgumentParseException e)
		{
			System.err.println(Resources.getString("CLIParseError"));
			return;
		}
		catch (NumberFormatException e)
		{
			System.err.println(Resources.getString("CLIParseError"));
			return;
		}
		catch (BadValueOfArgumentException e)
		{
			System.err.println(Resources.getString("CLIBadValueError"));
			return;
		}
		
		if (cmdLine.hasOption('h') || (cmdLine.hasOption('n') &&
				cmdLine.getArgs().length < 2))
		{
			helpFormatter.printHelp(Resources.getString("CLISyntax"), cliOptions);
			return;
		}
    	
		if (!cmdLine.hasOption('n'))
		{
    		SwingUtilities.invokeLater(new Runnable()
    		{
    			public void run()
    			{
    				createAndShowGUI();
    			}
    		});
		}
		else
		{
			String[] cmdArgs = cmdLine.getArgs();
			try
			{
    			FractalDocument fractal = new FractalDocument();
    			fractal.readFromFile(new File(cmdArgs[0]));
    			FractalRenderer renderer = new FractalRenderer
    					(outSize.width, outSize.height, aaConfig, fractal);
    			FractalImageWriter imageWriter = new FractalImageWriter(renderer, cmdArgs[1]);
    			
    			renderer.setThreadNumber(threadsNum);
    			renderer.prepareFractal();
    			if (printProgress)
    			{
    				renderer.addRenderProgressListener(new CMDLineProgressEventListener());
    				imageWriter.addImageWriterProgressListener(
    						new CMDLineImageWriteProgressListener());
    			}

    			imageWriter.write();
			}
			catch (Exception e)
			{ System.err.println(e.getMessage()); }
		}
	}

}
