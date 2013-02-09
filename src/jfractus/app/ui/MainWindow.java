/**
 * MainWindow.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

import jfractus.app.*;
import jfractus.app.FractusPreferencesEvent.Change;
import jfractus.app.ui.MessageDialog.MessageType;

import jfractus.math.*;

public class MainWindow extends JFrame
{
    private static final long serialVersionUID = -5899688560373717227L;

    /* Menus */
    private JMenuBar menuBar;
    
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu viewMenu;
    private JMenu windowMenu;
    private JMenu helpMenu;
    
    private JMenuItem fileNewItem;
    private JMenuItem fileOpenItem;
    private JMenuItem fileSaveItem;
    private JMenuItem fileSaveAsItem;
    private JMenuItem fileSaveImageItem;
    private JMenuItem filePrintItem;
    private JMenuItem fileCloseItem;
    private JMenuItem fileCloseAllItem;
    private JMenuItem fileExitItem;
    
    private JMenuItem editUndoItem;
    private JMenuItem editRedoItem;
    private JMenuItem editResetItem;
    private JMenuItem editPreferencesItem;
    private JMenuItem editRenderOptionsItem;
    private JMenuItem editGradientItem;
    
    private JMenuItem viewRefreshItem;
    private JCheckBoxMenuItem viewFormulaPanelItem;
    private JCheckBoxMenuItem viewToolbarItem;
    private JCheckBoxMenuItem viewTransformItem;
    private JRadioButtonMenuItem viewNormalItem;
    private JRadioButtonMenuItem viewInteractiveItem;
    
    private ButtonGroup viewModeGroup;
    
    private ButtonGroup windowItemGroup;
    
    private JMenuItem helpAboutItem;
    private JMenuItem helpKeysItem;
    
    private JToolBar toolBar;
    
    private JButton toolNewItem;
    private JButton toolOpenItem;
    private JButton toolSaveItem;
    private JButton toolCloseItem;
    private JButton toolUndoItem;
    private JButton toolRedoItem;
    private JButton toolResetItem;
    private JButton toolRefreshItem;
    
    private JComboBox toolImageSizeCombo;
    private JComboBox toolAntialiasCombo;
    
    private StatusBar statusBar;
    
    private ImageIcon jfractusIcon;
    
    private GradientEditor gradientEditor;
    private PreferencesDialog prefsDialog;
    private RenderOptionsDialog renderOptionsDialog;
    
    private JSplitPane splitPane;
    private FractalsTabbedPane tabbedPane;
    private FractalFunctionPanel formulaPanel;
    
    private boolean lockFractalState = false;
    private WindowMenuItemListener windowItemListener;
    
    private File currentFractalDirectory;
    private File currentImageDirectory;
    
    private FractalFrameChangeEventListener fractalFrameChangeListener;
    private FractalInteractionEventListener fractalInteractionListener;
    private ImageSaveErrorEventListener imageSaveErrorListener;
    private RenderingStateEventListener renderingStateListener;
    
    private boolean lockRenderCombo = false;
    
    private int newFractalIndex;
    
    
    private static class Size2D
    {
    	public int width;
    	public int height;
    	
    	public Size2D(int width, int height)
    	{
    		this.width = width;
    		this.height = height;
    	}
    	
    	public String toString()
    	{
    		return String.valueOf(width) + "x" + String.valueOf(height);
    	}
    	
    	public boolean equals(Object ob)
    	{
    		if (ob instanceof Size2D)
    		{
    			Size2D is = (Size2D)ob;
    			return width == is.width && height == is.height;
    		}
    		else return false;
    	}
    }
    
    private static final Size2D[] imageSizes =
    {
    	new Size2D(320, 240),
    	new Size2D(512, 384),
    	new Size2D(640, 480),
    	new Size2D(800, 600),
    	new Size2D(1024, 768),
    	new Size2D(1280, 720),
    	new Size2D(1280, 1024),
    	new Size2D(1600, 1200),
    	new Size2D(1920, 1080),
    	new Size2D(2560, 2048),
    	new Size2D(3200, 2400),
    };
    private static final Size2D[] antialiasSamplings =
    {
    	new Size2D(2, 2),
    	new Size2D(3, 3),
    	new Size2D(4, 4),
    };
    
    public MainWindow()
    {
    	newFractalIndex = 1;
    	currentFractalDirectory = new File(System.getProperty("user.dir"));
    	currentImageDirectory = new File(System.getProperty("user.dir"));
    	createGUI();
    	FractusPreferencesFactory.prefs.addPreferencesListener
    			(new FractusPreferencesEventListener());
    	updateRenderCombos(FractusPreferencesFactory.prefs.getDefaultImageWidth(),
	        			FractusPreferencesFactory.prefs.getDefaultImageHeight(),
	        			FractusPreferencesFactory.prefs.getDefaultAntialiasConfig());
    }
    
    private void createGUI()
    {
    	setSize(760, 580);
    	setTitle("JFractus");
    	jfractusIcon = new ImageIcon(Resources.getGraphics("icon-jfractus.png"));
    	setIconImage(jfractusIcon.getImage());
    	
    	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    	menuBar = new JMenuBar();
    	setJMenuBar(menuBar);
    	
    	fileMenu = new JMenu(Resources.getString("FileMenu"));
    	fileMenu.setMnemonic(Resources.getString("FileMenuMnemonic").charAt(0));
    	editMenu = new JMenu(Resources.getString("EditMenu"));
    	editMenu.setMnemonic(Resources.getString("EditMenuMnemonic").charAt(0));
    	viewMenu = new JMenu(Resources.getString("ViewMenu"));
    	viewMenu.setMnemonic(Resources.getString("ViewMenuMnemonic").charAt(0));
    	windowMenu = new JMenu(Resources.getString("WindowMenu"));
    	windowMenu.setMnemonic(Resources.getString("WindowMenuMnemonic").charAt(0));
    	helpMenu  = new JMenu(Resources.getString("HelpMenu"));
    	helpMenu.setMnemonic(Resources.getString("HelpMenuMnemonic").charAt(0));
    	
    	menuBar.add(fileMenu);
    	menuBar.add(editMenu);
    	menuBar.add(viewMenu);
    	menuBar.add(windowMenu);
    	menuBar.add(helpMenu);
    	
    	windowItemGroup = new ButtonGroup();
    	windowItemListener = new WindowMenuItemListener();
    	fractalFrameChangeListener = new FractalFrameChangeEventListener();
    	fractalInteractionListener = new FractalInteractionEventListener();
    	imageSaveErrorListener = new ImageSaveErrorEventListener();
    	renderingStateListener = new RenderingStateEventListener();
    	
    	/*
    	 * Menu File
    	 */
    	fileNewItem = new JMenuItem(Resources.getString("FileNew"),
    			Resources.getString("FileNewMnemonic").charAt(0));
    	fileNewItem.setIcon(new ImageIcon(Resources.getGraphics("icon-file-new.png")));
    	fileNewItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK));
    	fileNewItem.addActionListener(new FileNewActionListener());
    	fileMenu.add(fileNewItem);
    	
    	fileOpenItem = new JMenuItem(Resources.getString("FileOpen"),
    			Resources.getString("FileOpenMnemonic").charAt(0));
    	fileOpenItem.setIcon(new ImageIcon(Resources.getGraphics("icon-file-open.png")));
    	fileOpenItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK));
    	fileOpenItem.addActionListener(new FileOpenActionListener());
    	fileMenu.add(fileOpenItem);
    	
    	fileMenu.add(new JSeparator());
    	
    	fileSaveItem = new JMenuItem(Resources.getString("FileSave"),
    			Resources.getString("FileSaveMnemonic").charAt(0));
    	fileSaveItem.setIcon(new ImageIcon
    			(Resources.getGraphics("icon-file-save.png")));
    	fileSaveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
    	fileSaveItem.addActionListener(new FileSaveActionListener());
    	fileMenu.add(fileSaveItem);
    	
    	fileSaveAsItem = new JMenuItem(Resources.getString("FileSaveAs"),
    			Resources.getString("FileSaveAsMnemonic").charAt(0));
    	fileSaveAsItem.setIcon(new ImageIcon
    			(Resources.getGraphics("icon-file-save-as.png")));
    	fileSaveAsItem.setAccelerator(KeyStroke.getKeyStroke
    			('S', InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
    	fileSaveAsItem.addActionListener(new FileSaveAsActionListener());
    	fileMenu.add(fileSaveAsItem);
    	
    	fileSaveImageItem = new JMenuItem(Resources.getString("FileSaveImage"),
    			Resources.getString("FileSaveImageMnemonic").charAt(0));
    	/*fileSaveAsItem.setIcon(new ImageIcon
    			(Resources.getResource("icon-file-save-as.png")));*/
    	fileSaveImageItem.setAccelerator
    			(KeyStroke.getKeyStroke('I', InputEvent.CTRL_MASK));
    	fileSaveImageItem.addActionListener(new FileSaveImageActionListener());
    	fileMenu.add(fileSaveImageItem);
    	
    	fileMenu.add(new JSeparator());
    	
    	filePrintItem = new JMenuItem(Resources.getString("FilePrint"),
    			Resources.getString("FilePrintMnemonic").charAt(0));
    	filePrintItem.setIcon(new ImageIcon
    			(Resources.getGraphics("icon-file-print.png")));
    	filePrintItem.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_MASK));
    	filePrintItem.addActionListener(new FilePrintActionListener());
    	fileMenu.add(filePrintItem);
    	
    	fileMenu.add(new JSeparator());
    	
    	fileCloseItem = new JMenuItem(Resources.getString("FileClose"),
    			Resources.getString("FileCloseMnemonic").charAt(0));
    	fileCloseItem.setIcon(new ImageIcon(Resources.getGraphics("icon-file-close.png")));
    	fileCloseItem.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_MASK));
    	fileCloseItem.addActionListener(new FileCloseActionListener());
    	fileMenu.add(fileCloseItem);
    	
    	fileCloseAllItem = new JMenuItem(Resources.getString("FileCloseAll"),
    			Resources.getString("FileCloseAllMnemonic").charAt(0));
    	fileCloseAllItem.addActionListener(new FileCloseAllActionListener());
    	fileMenu.add(fileCloseAllItem);
    	
    	fileExitItem = new JMenuItem(Resources.getString("FileExit"),
    			Resources.getString("FileExitMnemonic").charAt(0));
    	fileExitItem.setIcon(new ImageIcon(Resources.getGraphics("icon-file-exit.png")));
    	fileExitItem.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_MASK));
    	fileExitItem.addActionListener(new FileExitActionListener());
    	fileMenu.add(fileExitItem);
    	
    	/*
    	 * Menu Edit
    	 */
    	editUndoItem = new JMenuItem(Resources.getString("EditUndo"),
    			Resources.getString("EditUndoMnemonic").charAt(0));
    	editUndoItem.setIcon(new ImageIcon(Resources.getGraphics("icon-edit-undo.png")));
    	editUndoItem.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK));
    	editUndoItem.addActionListener(new EditUndoActionListener());
    	editMenu.add(editUndoItem);
    	
    	editRedoItem = new JMenuItem(Resources.getString("EditRedo"),
    			Resources.getString("EditRedoMnemonic").charAt(0));
    	editRedoItem.setIcon(new ImageIcon(Resources.getGraphics("icon-edit-redo.png")));
    	editRedoItem.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_MASK));
    	editRedoItem.addActionListener(new EditRedoActionListener());
    	editMenu.add(editRedoItem);
    	
    	editMenu.add(new JSeparator());
    	
    	editResetItem = new JMenuItem(Resources.getString("EditReset"),
    			Resources.getString("EditResetMnemonic").charAt(0));
    	editResetItem.setIcon(new ImageIcon(Resources.getGraphics("icon-home.png")));
    	editResetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
    	editResetItem.addActionListener(new EditResetActionListener());
    	editMenu.add(editResetItem);
    	
    	editMenu.add(new JSeparator());
    	
    	editPreferencesItem = new JMenuItem(Resources.getString("EditPreferences"),
    			Resources.getString("EditPreferencesMnemonic").charAt(0));
    	editPreferencesItem.setIcon(new ImageIcon
    			(Resources.getGraphics("icon-preferences.png")));
    	editPreferencesItem.addActionListener(new EditPreferencesActionListener());
    	editMenu.add(editPreferencesItem);
    	
    	editRenderOptionsItem = new JMenuItem(Resources.getString("EditRenderOptions"),
    			Resources.getString("EditRenderOptionsMnemonic").charAt(0));
    	editRenderOptionsItem.addActionListener(new EditRenderOptionsActionListener());
    	editMenu.add(editRenderOptionsItem);
    	
    	editMenu.add(new JSeparator());
    	
    	editGradientItem = new JMenuItem(Resources.getString("EditGradient"),
    			Resources.getString("EditGradientMnemonic").charAt(0));
    	editGradientItem.setIcon(new ImageIcon
    			(Resources.getGraphics("icon-edit-gradients.png")));
    	editGradientItem.setAccelerator(KeyStroke.getKeyStroke('G', InputEvent.CTRL_MASK));
    	editGradientItem.addActionListener(new EditGradientActionListener());
    	editMenu.add(editGradientItem);
    	
    	/*
    	 * Menu View
    	 */
    	viewRefreshItem = new JMenuItem(Resources.getString("ViewRefresh"),
    			Resources.getString("ViewRefreshMnemonic").charAt(0));
    	viewRefreshItem.setIcon(new ImageIcon
    			(Resources.getGraphics("icon-view-refresh.png")));
    	viewRefreshItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK));
    	viewRefreshItem.addActionListener(new ViewRefreshActionListener());
    	viewMenu.add(viewRefreshItem);
    	
    	viewMenu.add(new JSeparator());
    	
    	viewFormulaPanelItem = new JCheckBoxMenuItem
    			(Resources.getString("ViewFormulaPanel"));
    	viewFormulaPanelItem.setSelected(true);
    	viewFormulaPanelItem.setMnemonic(
    			Resources.getString("ViewFormulaPanelMnemonic").charAt(0));
    	viewFormulaPanelItem.setAccelerator(
    			KeyStroke.getKeyStroke('F', InputEvent.CTRL_MASK));
    	viewFormulaPanelItem.addItemListener (new ViewFormulaPanelItemListener());
    	viewMenu.add(viewFormulaPanelItem);
    	
    	viewToolbarItem = new JCheckBoxMenuItem(Resources.getString("ViewToolbar"));
    	viewToolbarItem.setSelected(true);
    	viewToolbarItem.setMnemonic(Resources.getString("ViewToolbarMnemonic").charAt(0));
    	viewToolbarItem.addItemListener(new ViewToolbarItemListener());
    	viewMenu.add(viewToolbarItem);
    	
    	viewTransformItem = new JCheckBoxMenuItem(Resources.getString("ViewTransform"));
    	viewTransformItem.setMnemonic(Resources.getString("ViewTransformMnemonic").charAt(0));
    	viewTransformItem.addItemListener(new ViewTransformItemListener());
    	viewMenu.add(viewTransformItem);
    	
    	viewMenu.add(new JSeparator());
    	
    	viewModeGroup = new ButtonGroup();
    	
    	viewNormalItem = new JRadioButtonMenuItem(Resources.getString("ViewNormalMode"));
    	viewNormalItem.setMnemonic
    			(Resources.getString("ViewNormalModeMnemonic").charAt(0));
    	viewNormalItem.setAccelerator(KeyStroke.getKeyStroke('1', InputEvent.CTRL_MASK));
    	viewNormalItem.addItemListener(new ViewNormalItemListener());
    	viewMenu.add(viewNormalItem);
    	viewModeGroup.add(viewNormalItem);
    	
    	viewInteractiveItem = new JRadioButtonMenuItem
    			(Resources.getString("ViewInteractiveMode"));
    	viewInteractiveItem.setMnemonic
    			(Resources.getString("ViewInteractiveModeMnemonic").charAt(0));
    	viewInteractiveItem.setAccelerator
    			(KeyStroke.getKeyStroke('2', InputEvent.CTRL_MASK));
    	viewInteractiveItem.addItemListener(new ViewInteractiveItemListener());
    	viewMenu.add(viewInteractiveItem);
    	viewModeGroup.add(viewInteractiveItem);
    	
    	/*
    	 * Menu Help
    	 */
    	helpAboutItem = new JMenuItem(Resources.getString("HelpAbout"),
    			Resources.getString("HelpAboutMnemonic").charAt(0));
    	helpAboutItem.setIcon(new ImageIcon
    			(Resources.getGraphics("icon-help-about.png")));
    	helpAboutItem.addActionListener(new HelpAboutActionListener());
    	helpMenu.add(helpAboutItem);
    	
    	helpMenu.add(new JSeparator());
    	
    	helpKeysItem = new JMenuItem(Resources.getString("HelpKeys"),
    			Resources.getString("HelpKeysMnemonic").charAt(0));
    	helpKeysItem.addActionListener(new HelpKeysActionListener());
    	helpMenu.add(helpKeysItem);
    	
    	/*
    	 * ToolBar
    	 */
    	toolBar = new JToolBar();
    	
    	toolNewItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-file-new.png")));
    	toolNewItem.addActionListener(new FileNewActionListener());
    	toolNewItem.setToolTipText(Resources.getString("NewToolTip"));
    	toolBar.add(toolNewItem);
    	
    	toolOpenItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-file-open.png")));
    	toolOpenItem.addActionListener(new FileOpenActionListener());
    	toolOpenItem.setToolTipText(Resources.getString("OpenToolTip"));
    	toolBar.add(toolOpenItem);
    	
    	toolSaveItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-file-save.png")));
    	toolSaveItem.addActionListener(new FileSaveActionListener());
    	toolSaveItem.setToolTipText(Resources.getString("SaveToolTip"));
    	toolBar.add(toolSaveItem);
    	
    	toolBar.addSeparator();
    	
    	toolCloseItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-file-close.png")));
    	toolCloseItem.addActionListener(new FileCloseActionListener());
    	toolCloseItem.setToolTipText(Resources.getString("CloseToolTip"));
    	toolBar.add(toolCloseItem);
    	
    	toolBar.addSeparator();
    	
    	toolUndoItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-edit-undo.png")));
    	toolUndoItem.addActionListener(new EditUndoActionListener());
    	toolUndoItem.setToolTipText(Resources.getString("UndoToolTip"));
    	toolBar.add(toolUndoItem);
    	
    	toolRedoItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-edit-redo.png")));
    	toolRedoItem.addActionListener(new EditRedoActionListener());
    	toolRedoItem.setToolTipText(Resources.getString("RedoToolTip"));
    	toolBar.add(toolRedoItem);
    	
    	toolBar.addSeparator();
    	
    	toolResetItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-home.png")));
    	toolResetItem.addActionListener(new EditResetActionListener());
    	toolResetItem.setToolTipText(Resources.getString("ResetToolTip"));
    	toolBar.add(toolResetItem);
    	
    	toolBar.addSeparator();
    	
    	toolRefreshItem = new JButton(new ImageIcon
    			(Resources.getGraphics("icon-view-refresh.png")));
    	toolRefreshItem.addActionListener(new ViewRefreshActionListener());
    	toolRefreshItem.setToolTipText(Resources.getString("RefreshToolTip"));
    	toolBar.add(toolRefreshItem);
    	
    	toolBar.addSeparator();
    	
    	toolBar.add(new JLabel(Resources.getString("SizeLabel")));
    	
    	RenderComboActionListener renderComboActionListener = 
    		new RenderComboActionListener();
    	toolImageSizeCombo = new JComboBox();
    	for (Size2D s: imageSizes)
    		toolImageSizeCombo.addItem(s);
    	toolImageSizeCombo.addItem(Resources.getString("Other"));
    	toolImageSizeCombo.addActionListener(renderComboActionListener);
    	toolImageSizeCombo.setMaximumSize(new Dimension(100, 24));
    	toolBar.add(toolImageSizeCombo);
    	
    	toolBar.add(new JLabel(Resources.getString("AALabel")));
    	
    	toolAntialiasCombo = new JComboBox();
    	toolAntialiasCombo.addItem(Resources.getString("AANone"));
    	for (Size2D s: antialiasSamplings)
    		toolAntialiasCombo.addItem(s);
    	toolAntialiasCombo.addItem(Resources.getString("Other"));
    	toolAntialiasCombo.addActionListener(renderComboActionListener);
    	toolAntialiasCombo.setMaximumSize(new Dimension(100, 24));
    	toolBar.add(toolAntialiasCombo);

    	/* others  */
    	gradientEditor = new GradientEditor(this);
    	prefsDialog = new PreferencesDialog(this);
    	
    	tabbedPane = new FractalsTabbedPane();
    	tabbedPane.addChangeListener(new FractalsTabChangeListener());
    	formulaPanel = new FractalFunctionPanel(this);
    	renderOptionsDialog = new RenderOptionsDialog(this);
    	renderOptionsDialog.addRenderOptionsChangeListener
    			(new RenderOptionsChangeEventListener());
    	
    	formulaPanel.getGradientTab().setGradientEditor(gradientEditor);
    	formulaPanel.getGradientTab().addGradientChangeListener
    			(new GradientChangeEventListener());
    	formulaPanel.addFunctionChangeListener(new FunctionChangeEventListener());
    	formulaPanel.setMinimumSize(new Dimension(200, 300));
    	
    	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
    			tabbedPane, formulaPanel);
    	splitPane.setResizeWeight(1.0);
    	splitPane.setDividerLocation(400);
    	
    	JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());
    	
    	setContentPane(panel);
    	panel.add(toolBar, BorderLayout.NORTH);
    	panel.add(splitPane, BorderLayout.CENTER);
    	
    	statusBar = new StatusBar(this);
    	panel.add(statusBar, BorderLayout.SOUTH);
    	
    	updateMenuAndToolbarItemsEnabling();
    	updateFractalFormulaEnabling();
    }
    
    /* updating menu items */
    
    private void updateMenuAndToolbarItemsEnabling()
    {
    	if (tabbedPane.getTabCount() == 0)
    	{
    		fileSaveAsItem.setEnabled(false);
    		toolSaveItem.setEnabled(false);
    		fileSaveItem.setEnabled(false);
    		fileSaveImageItem.setEnabled(false);
    		filePrintItem.setEnabled(false);
    		fileCloseItem.setEnabled(false);
    		toolCloseItem.setEnabled(false);
    		fileCloseAllItem.setEnabled(false);
    		toolUndoItem.setEnabled(false);
    		editUndoItem.setEnabled(false);
    		toolRedoItem.setEnabled(false);
    		editRedoItem.setEnabled(false);
    		editResetItem.setEnabled(false);
    		viewRefreshItem.setEnabled(false);
    		viewTransformItem.setEnabled(false);
    		viewInteractiveItem.setEnabled(false);
    		viewNormalItem.setEnabled(false);
    	}
    	else
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		fileSaveAsItem.setEnabled(true);
    		fileSaveItem.setEnabled(true);
    		toolSaveItem.setEnabled(true);
    		boolean renderFinished = panel.isRenderingFinished();
    		fileSaveImageItem.setEnabled(renderFinished);
    		filePrintItem.setEnabled(renderFinished);
    		toolCloseItem.setEnabled(true);
    		fileCloseItem.setEnabled(true);
    		fileCloseAllItem.setEnabled(true);
    		editUndoItem.setEnabled(panel.hasUndo());
    		toolUndoItem.setEnabled(panel.hasUndo());
    		editRedoItem.setEnabled(panel.hasRedo());
    		toolRedoItem.setEnabled(panel.hasRedo());
    		editResetItem.setEnabled(true);
    		viewRefreshItem.setEnabled(true);
    		viewTransformItem.setEnabled(true);
    		viewInteractiveItem.setEnabled(true);
    		viewNormalItem.setEnabled(true);
    	}
    }
    
    private void updateFractalFormulaEnabling()
    {
    	formulaPanel.setEnabled(tabbedPane.getTabCount() != 0);
    }
    
    /* Window menu managing methods */
    private void addFractalToWindowMenu(String name)
    {
    	JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
    	item.addItemListener(windowItemListener);
    	windowItemGroup.add(item);
    	windowMenu.add(item);
    	selectFractalInWindowMenu(windowMenu.getItemCount()-1);
    }
    private void renameFractalInWindowMenu(int index, String name)
    {
    	JRadioButtonMenuItem item =
    		(JRadioButtonMenuItem)windowMenu.getMenuComponent(index);
    	item.setText(name);
    }
    private void selectFractalInWindowMenu(int index)
    {
    	if (index >= windowMenu.getItemCount())
    		return;
    	JRadioButtonMenuItem item =
    		(JRadioButtonMenuItem)windowMenu.getMenuComponent(index);
    	item.setSelected(true);
    }
    private void removeFractalFromWindowMenu(int index)
    {
    	windowMenu.remove(index);
    }
    private void removeAllFromWindowMenu()
    {
    	windowMenu.removeAll();
    }
    
    private void setupFractalPanel(FractalPanel panel)
    {
    	panel.addFractalFrameChangeListener(fractalFrameChangeListener);
    	panel.addFractalInteractionListener(fractalInteractionListener);
    	panel.addImageSaveErrorListener(imageSaveErrorListener);
    	panel.addRenderingStateListener(renderingStateListener);
    }
    
    private void saveFractal()
    {
    	FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    	if (panel == null)
    		return;
    	
    	if (panel.getFilePath() != null)
    	{
    		try
			{
    			FractalSaveOptionsDialog dialog = new FractalSaveOptionsDialog(this);
            	if (dialog.run() != Stock.OK)
            		return;

    			panel.saveToFile(dialog.isSaveRelative());
			}
    		catch(Exception ex)
    		{ runErrorDialog(Resources.getString("SaveError"), ex); }
    	}
    	else
    		saveAsFractal();
    }
    
    private void saveAsFractal()
    {
    	FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    	if (panel == null)
    		return;
    	
    	JFileChooser fileChooser = new JFileChooser(currentFractalDirectory);
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				Resources.getString("Fractal"), "frcx"));
		
		int returnVal = fileChooser.showSaveDialog(MainWindow.this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			try
			{
				FractalSaveOptionsDialog dialog = new FractalSaveOptionsDialog(this);
            	if (dialog.run() != Stock.OK)
            		return;
				panel.saveToFile(file, dialog.isSaveRelative());
				int panelIndex = tabbedPane.getSelectedIndex();
				tabbedPane.setTitleAt(panelIndex, file.getName());
				renameFractalInWindowMenu(panelIndex, file.getName());
				currentFractalDirectory = fileChooser.getCurrentDirectory();
			}
			catch(Exception ex)
			{ runErrorDialog(Resources.getString("SaveError"), ex); }
		}
    }
    
    private void runErrorDialog(String title, Exception ex)
    {
    	MessageDialog dialog = new MessageDialog(MainWindow.this,
				MessageType.ERROR, MessageDialog.ResponseType.OK, title,
				ex.getClass().getName() + ":" + ex.getMessage());
		dialog.run();
    }
    
    /* File menu listeners */
    
    private class FileNewActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		String newFractalName = Resources.getString("NewFractal") +
    				" " + newFractalIndex;
    		newFractalIndex++;
    		FractalPanel panel = new FractalPanel(formulaPanel);
    		tabbedPane.addFractal(newFractalName, panel);
    		setupFractalPanel(panel);
    		addFractalToWindowMenu(newFractalName);
    		
    		updateMenuAndToolbarItemsEnabling();
    		updateFractalFormulaEnabling();
    	}
    }
    
    private class FileOpenActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		JFileChooser fileChooser = new JFileChooser(currentFractalDirectory);
    		fileChooser.setFileFilter(new FileNameExtensionFilter(
    				Resources.getString("Fractal"), "frcx"));
    		
    		int returnVal = fileChooser.showOpenDialog(MainWindow.this);
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			File file = fileChooser.getSelectedFile();
    			try
    			{
    				FractalPanel panel = new FractalPanel(formulaPanel, file);
    				tabbedPane.addFractal(file.getName(), panel);
    				addFractalToWindowMenu(file.getName());
    				setupFractalPanel(panel);
    				currentFractalDirectory = fileChooser.getCurrentDirectory();
    			}
    			catch(Exception ex)
    			{ runErrorDialog(Resources.getString("OpenError"), ex); }
    			
    			updateMenuAndToolbarItemsEnabling();
    			updateFractalFormulaEnabling();
    		}
    	}
    }
    
    private class FileSaveActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		saveFractal();
    	}
    }
    
    private class FileSaveAsActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		saveAsFractal();
    	}
    }
    
    private class FileSaveImageActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (panel != null && panel.isRenderingFinished())
    		{
    			JFileChooser fileChooser = new JFileChooser(currentImageDirectory);
    			String[] suffixes = ImageIO.getWriterFileSuffixes();
    			for (String suffix: suffixes)
    			{
    				fileChooser.addChoosableFileFilter
    					(new FileNameExtensionFilter(suffix, suffix));
    			}
    			fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[1]);
    			
    			int returnVal = fileChooser.showSaveDialog(MainWindow.this);
    			if (returnVal != JFileChooser.APPROVE_OPTION)
    				return;
    			
    			{
    				String filePath = fileChooser.getSelectedFile().getPath();
    				ImageWriteParam writeParam = FractalImageWriter.getDefaultWriteParam
    						(FractalImageWriter.getFilenameSuffix(filePath));
    				
    				ImageSaveOptionsDialog optionsDialog =
    						new ImageSaveOptionsDialog(MainWindow.this, writeParam);
    				if (optionsDialog.run() != Stock.OK)
    					return;
    				writeParam = optionsDialog.getImageWriteParam();
    				
    				panel.saveImageFile(fileChooser.getSelectedFile(), writeParam);
    				
    				currentImageDirectory = fileChooser.getCurrentDirectory();
    			}
    		}
    	}
    }
    
    private class ImagePrintable implements Printable
    {
    	private BufferedImage image;
    	
    	public ImagePrintable(BufferedImage image)
    	{
    		this.image = image;
    	}

		@Override
        public int print(Graphics gx, PageFormat pageFormat, int pageIndex)
                throws PrinterException
        {
	        if (pageIndex == 0)
	        {
	        	Graphics2D g = (Graphics2D)gx;
	        	g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	        	
	        	float xScaleFactor = (float)pageFormat.getImageableWidth() /
	        		(float)image.getWidth();
	        	float yScaleFactor = (float)pageFormat.getImageableHeight() /
	        		(float)image.getHeight();
	        	float scale = Math.min(xScaleFactor, yScaleFactor);
	        	
	        	System.out.println("print scale:" + scale);
	        	System.out.println("printMatrix:" + new AffineTransform(pageFormat.getMatrix()));
	        	
	        	g.drawImage(image,
	        			new AffineTransform(scale, 0.0, 0.0, scale, 0.0, 0.0), null);
	        	return Printable.PAGE_EXISTS;
	        }
	        else
	        	return Printable.NO_SUCH_PAGE;
        }
    	
    }
    
    private class FilePrintActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (panel != null && panel.isRenderingFinished())
    		{
    			ImagePrintable printable = new ImagePrintable(panel.getRenderedImage());
    			
    			PrintRequestAttributeSet attrSet = new HashPrintRequestAttributeSet();
    			attrSet.add(OrientationRequested.LANDSCAPE);
    			attrSet.add(new Copies(1));
    			attrSet.add(new JobName("Fractal", null));
    			
    			PrinterJob printerJob = PrinterJob.getPrinterJob();
    			printerJob.setPrintable(printable);
    			
    			PrintService[] services = PrinterJob.lookupPrintServices();
    			
    			if(services.length > 0)
    			{
    				try
    				{
    					printerJob.setPrintService(services[0]);
    					if (printerJob.printDialog(attrSet))
    						printerJob.print();
    				}
    				catch(Exception ex)
    				{ runErrorDialog(Resources.getString("PrintError"), ex); }
    			}
    		}
    	}
    }
    
    private class FileCloseActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		int panelIndex = tabbedPane.getSelectedIndex();
    		if (panelIndex < 0)
    			return;
    		
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		panel.cancelRender();
    		
    		removeFractalFromWindowMenu(panelIndex);
    		tabbedPane.removeTabAt(panelIndex);
    		
    		updateMenuAndToolbarItemsEnabling();
    		updateFractalFormulaEnabling();
    	}
    }
    
    private class FileCloseAllActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		for (int i = 0; i < tabbedPane.getTabCount(); i++)
    			tabbedPane.getFractalPanel(i).cancelRender();
    		
    		tabbedPane.removeAll();
    		removeAllFromWindowMenu();
    		
    		updateMenuAndToolbarItemsEnabling();
    		updateFractalFormulaEnabling();
    	}
    }
    
    private class FileExitActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		dispose();
    	}
    }
    
    /* Edit menu listeners */
    
    private class EditUndoActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (panel != null)
    		{
    			panel.undo();
    			updateMenuAndToolbarItemsEnabling();
    		}
    	}
    }
    
    private class EditRedoActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (panel != null)
    		{
    			panel.redo();
    			updateMenuAndToolbarItemsEnabling();
    		}
    	}
    }
    
    private class EditResetActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (panel != null)
    			panel.resetTransform();
    	}
    }
    
    private class EditPreferencesActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		prefsDialog.runPreferences();
    	}
    }
    
    private class EditRenderOptionsActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		renderOptionsDialog.runOptionsDialog(tabbedPane.getSelectedFractalPanel());
    	}
    }
    
    private class EditGradientActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		formulaPanel.getGradientTab().editGradient();
    	}
    }
    
    /* View menu listeners */
    
    private class ViewRefreshActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (panel != null)
    			panel.doRender();
    	}
    }
    
    private class ViewFormulaPanelItemListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		if (viewFormulaPanelItem.isSelected())
    		{
    			formulaPanel.setVisible(true);
    			splitPane.setDividerLocation(400);
    		}
    		else formulaPanel.setVisible(false);
    	}
    }
    
    private class ViewToolbarItemListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		toolBar.setVisible(viewToolbarItem.isSelected());
    	}
    }
    
    private class ViewTransformItemListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (panel != null)
    			panel.setVisibleTransformPanel(e.getStateChange() == ItemEvent.SELECTED);
    	}
    }
    
    private class ViewNormalItemListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    	}
    }
    
    private class ViewInteractiveItemListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    	}
    }
    
    /* Help menu listeners */
    
    private class HelpAboutActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		AboutDialog dialog = new AboutDialog(MainWindow.this);
    		dialog.run();
    	}
    }
    
    private class HelpKeysActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		KeysListDialog dialog = new KeysListDialog(MainWindow.this);
    		dialog.run();
    	}
    }
    
    /* Window menu listener */
    
    private class WindowMenuItemListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		if(lockFractalState || e.getStateChange() == ItemEvent.DESELECTED)
    			return;
    		Component[] components = windowMenu.getMenuComponents();
    		int found = 0;
    		for(found = 0; found < components.length; found++)
    			if (components[found] == e.getItemSelectable())
    				break;
    		if (found < components.length)
    			tabbedPane.setSelectedIndex(found);
    	}
    }
    
    private class FractalsTabChangeListener implements ChangeListener
    {

		public void stateChanged(ChangeEvent e)
        {
			//System.out.println("Updating from FractalDocument");
			FractalPanel panel = tabbedPane.getSelectedFractalPanel();
			if (panel == null)
			{
				updateRenderCombos(FractusPreferencesFactory.prefs.getDefaultImageWidth(),
	        			FractusPreferencesFactory.prefs.getDefaultImageHeight(),
	        			FractusPreferencesFactory.prefs.getDefaultAntialiasConfig());
				setTitle("JFractus");
				return;
			}
			
			formulaPanel.setFromFractalDocument(panel.getFractal());
			Matrix3D m = panel.getFractal().getFractalFrame().getTransform();
    		statusBar.setPosition(new Complex(m.m[2], m.m[5]));
    		viewTransformItem.setSelected(panel.isVisibleTransformPanel());
    		updateRenderCombos(panel.getImageWidth(), panel.getImageHeight(),
    				panel.getAntialiasConfig());
    		
    		setTitle("JFractus - " + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
    		
    		updateMenuAndToolbarItemsEnabling();
	    }
    }
    
    /* render options combo listeners */
    
    private class RenderComboActionListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if (lockRenderCombo)
    			return;
    		
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		
    		Size2D imageSize = null;
    		AntialiasConfig aaConfig = null;
    		if (toolImageSizeCombo.getSelectedItem() instanceof Size2D)
    			imageSize = (Size2D)toolImageSizeCombo.getSelectedItem();
    		else
    			imageSize = new Size2D(renderOptionsDialog.getImageWidth(),
    						renderOptionsDialog.getImageHeight());
    		
    		if (toolAntialiasCombo.getSelectedItem() instanceof Size2D)
    		{
    			Size2D sampling = (Size2D)toolAntialiasCombo.getSelectedItem();
    			aaConfig = new AntialiasConfig(AntialiasConfig.Method.NORMAL,
    					sampling.width, sampling.height);
    		}
    		else
    		{
    			String s = (String)toolAntialiasCombo.getSelectedItem();
    			if (s.equals(Resources.getString("Other")))
    				aaConfig = renderOptionsDialog.getAntialiasConfig();
    			else
    				aaConfig = new AntialiasConfig(AntialiasConfig.Method.NONE, 1, 1);
    		}
    			
    		
    		if (panel != null)
    			panel.applyRenderOptions(imageSize.width, imageSize.height, aaConfig);
    		FractusPreferencesFactory.prefs.setDefaultImageSize(imageSize.width,
    				imageSize.height);
    		FractusPreferencesFactory.prefs.setDefaultAntialiasConfig(aaConfig);
    	}
    }
    
    private void updateRenderCombos(int imageWidth, int imageHeight,
    		AntialiasConfig aaConfig)
    {
    	lockRenderCombo = true;
    	int match = 0;
    	for (match = 0; match < imageSizes.length; match++)
    		if (imageSizes[match].width == imageWidth &&
    				imageSizes[match].height == imageHeight)
    			break;
    	toolImageSizeCombo.setSelectedIndex(match);
    	
    	if (aaConfig.getMethod() == AntialiasConfig.Method.NORMAL)
    	{
        	int aaWidth = aaConfig.getSamplingWidth();
        	int aaHeight = aaConfig.getSamplingHeight();
        	for (match = 0; match < antialiasSamplings.length; match++)
        		if (antialiasSamplings[match].width == aaWidth &&
        				antialiasSamplings[match].height == aaHeight)
        			break;
        	
        	toolAntialiasCombo.setSelectedIndex(match+1);
    	}
    	else if (aaConfig.getMethod() == AntialiasConfig.Method.NORMAL)
    		toolAntialiasCombo.setSelectedIndex(antialiasSamplings.length+1);
    	else
    		toolAntialiasCombo.setSelectedIndex(0);
    	
    	lockRenderCombo = false;
    }
    
    private class FractusPreferencesEventListener implements FractusPreferencesListener
    {
        public void preferencesChanged(FractusPreferencesEvent e)
        {
	        if (e.getChange() == Change.DEFAULT_AA_CONFIG ||
	        		e.getChange() == Change.DEFAULT_IMAGE_SIZE)
	        {
	        	updateRenderCombos(FractusPreferencesFactory.prefs.getDefaultImageWidth(),
	        			FractusPreferencesFactory.prefs.getDefaultImageHeight(),
	        			FractusPreferencesFactory.prefs.getDefaultAntialiasConfig());
	        }
        }
    	
    }
    
    /* other Listeners */
    private class GradientChangeEventListener implements GradientChangeListener
    {
    	public void gradientChanged(GradientChangeEvent e)
    	{
    		FractalPanel selected = tabbedPane.getSelectedFractalPanel();
    		if (selected != null)
    			selected.setGradientAndColoring(e.getColorScale(), e.getColorShift(),
    					e.getGradient(), e.getGradientLocator());
    	}
    }
    
    private class RenderOptionsChangeEventListener implements RenderOptionsChangeListener
    {
    	public void renderOptionsChanged(RenderOptionsChangeEvent e)
    	{
    		tabbedPane.getSelectedFractalPanel().applyRenderOptions
    				(e.getImageWidth(), e.getImageHeight(), e.getAntialiasConfig());
    		updateRenderCombos(e.getImageWidth(), e.getImageHeight(), e.getAntialiasConfig());
    	}
    }
    
    private class FunctionChangeEventListener implements FunctionChangeListener
    {
    	public void functionChanged(FunctionChangeEvent e)
    	{
    		FractalPanel selected = tabbedPane.getSelectedFractalPanel();
    		if (selected != null)
    			selected.setFunction(e.getFunctionType(), e.getFunction());
    	}
    }
    
    private class FractalFrameChangeEventListener implements FractalFrameChangeListener
    {
    	public void fractalFrameChanged(FractalFrameChangeEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (e.getSource() != panel.getFractalViewer())
    			return;
    		Matrix3D m = e.getFractalFrame().getTransform();
    		statusBar.setPosition(new Complex(m.m[2], m.m[5]));
    	}
    }
    
    private class FractalInteractionEventListener implements FractalInteractionListener
    {
    	public void interactionPerformed(FractalInteractionEvent e)
    	{
    		FractalPanel panel = tabbedPane.getSelectedFractalPanel();
    		if (e.getSource() != panel.getFractalViewer())
    			return;
    		
    		int interactionStep = e.getInteractionStep();
    		TransformType transformType = e.getTransformType();
    		if ((interactionStep == 2 &&
    				(transformType != TransformType.TRANSFORM &&
    					transformType != TransformType.INVERSE_TRANSFORM)) ||
    					interactionStep == 3 ||
    					interactionStep == 0)
    		{
    			statusBar.setMessage("");
    			return;
    		}
    		
    		switch(transformType)
    		{
    		case RECTANGLE_ZOOMIN:
    		case NORMAL_ZOOMIN:
    			statusBar.setMessage(Resources.getString("ZoomInFractal"));
    			break;
    		case RECTANGLE_ZOOMOUT:
    		case NORMAL_ZOOMOUT:
    			statusBar.setMessage(Resources.getString("ZoomOutFractal"));
    			break;
    		case ROTATE:
    			statusBar.setMessage(Resources.getString("RotateFractal"));
    			break;
    		case TRANSLATE:
    			statusBar.setMessage(Resources.getString("TranslateFractal"));
    			break;
    		case TRANSFORM:
    			statusBar.setMessage(Resources.getString("TransformFractal"));
    			break;
    		case INVERSE_TRANSFORM:
    			statusBar.setMessage(Resources.getString("InverseTransformFractal"));
    			break;
    		case NONE:
    			statusBar.setMessage("");
    			break;
    		default:
    			break;
    		}
    	}
    }
    
    private class RenderingStateEventListener implements RenderingStateListener
    {
    	public void renderingFinished(RenderingStateEvent e)
    	{
    		updateMenuAndToolbarItemsEnabling();
    	}
    }
    
    private class ImageSaveErrorEventListener implements ImageSaveErrorListener
    {
    	public void errorOccured(ImageSaveErrorEvent e)
    	{
    		runErrorDialog(Resources.getString("SaveImageError"), e.getException());
    	}
    }
}
