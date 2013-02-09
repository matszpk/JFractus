/**
 * Complex.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.math;

import java.io.Serializable;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import jfractus.dom.*;
import jfractus.generic.Copyable;

public class Complex implements Serializable, Copyable<Complex>, DOMNodeHandler
{
	private static final long serialVersionUID = -6641858491384984392L;
	
    public double re, im;
	
	public Complex()
	{
		re = im = 0.0;
	}
	protected Complex(Complex c)
	{
		re = c.re;
		im = c.im;
	}
	
	public Complex(double r)
	{
		re = r;
		im = 0.0;
	}
	public Complex(double re, double im)
	{
		this.re = re;
		this.im = im;
	}
	
	/* copy method */
	public Complex copy()
	{
		return new Complex(this);
	}
	
	/* set method */
	public void set(double r)
	{
		re = r;
		im = 0.0;
	}
	public void set(double re, double im)
	{
		this.re = re;
		this.im = im;
	}
	public void set(Complex c)
	{
		re = c.re;
		im = c.im;
	}
	
	/* all following methods modify Complex number and return it value */
	public Complex add(double r)
	{
		re += r;
		return this;
	}
	public Complex add(double r, double i)
	{
		re += r;
		im += i;
		return this;
	}
	public Complex add(Complex c)
	{
		re += c.re;
		im += c.im;
		return this;
	}
	
	public Complex subtract(double r)
	{
		re -= r;
		return this;
	}
	public Complex subtract(double r, double i)
	{
		re -= r;
		im -= i;
		return this;
	}
	public Complex subtract(Complex c)
	{
		re -= c.re;
		im -= c.im;
		return this;
	}
	
	public Complex multiply(double r)
	{
		re *= r;
		im *= r;
		return this;
	}
	public Complex multiply(double r, double i)
	{
		double tmp = re;
		re = re*r - im*i;
		im = tmp*i + im*r;
		return this;
	}
	public Complex multiply(Complex c)
	{
		double tmp = re;
		re = re*c.re - im*c.im;
		im = tmp*c.im + im*c.re;
		return this;
	}
	
	public Complex divide(double r)
	{
		re /= r;
		im /= r;
		return this;
	}
	public Complex divide(double r, double i)
	{
		double norm2 = (r*r + i*i);
		double tmp = re;
		re = (re*r + im*i) / norm2;
		im = (im*r - tmp*i) / norm2;
		return this;
	}
	public Complex divide(Complex c)
	{
		double norm2 = (c.re*c.re + c.im*c.im);
		double tmp = re;
		re = (re*c.re + im*c.im) / norm2;
		im = (im*c.re - tmp*c.im) / norm2;
		return this;
	}
	
	public Complex neg()
	{
		re = -re;
		im = -im;
		return this;
	}
	
	public Complex conj()
	{
		im = -im;
		return this;
	}
	public double abs()
	{
		return Math.hypot(re, im); 
	}
	public double arg()
	{
		if (re != 0.0 || im != 0.0)
		{
			if (re >= 0.0 || im != 0.0)
				return Math.acos(re/Math.hypot(re, im))*Math.signum(im);
			else
				return Math.PI;
		}
		else
			return 0.0;
	}
	
	public double norm2()
	{
		return re*re + im*im;
	}
	
	public Complex recip()
	{
		double norm2 = re*re + im*im;
		re /= norm2;
		im /= -norm2;
		return this;
	}
	
	public Complex exp()
	{
		double rexp = Math.exp(re);
		re = Math.cos(im) * rexp;
		im = Math.sin(im) * rexp;
		return this;
	}
	
	public Complex pow(double r)
	{
		if (re == 0.0 && im == 0.0)
			return this;
		log();
		re *= r;
		im *= r;
		double rexp = Math.exp(re);
		re = Math.cos(im) * rexp;
		im = Math.sin(im) * rexp;
		return this;
	}
	public Complex pow(double r, double i)
	{
		if (re == 0.0 && im == 0.0)
			return this;

		log();
		double tmp = re;
		re = re*r - im*i;
		im = tmp*i + im*r;
		double rexp = Math.exp(re);
		re = Math.cos(im) * rexp;
		im = Math.sin(im) * rexp;
		return this;
	}
	public Complex pow(Complex c)
	{
		if (re == 0.0 && im == 0.0)
			return this;

		log();
		double tmp = re;
		re = re*c.re - im*c.im;
		im = tmp*c.im + im*c.re;
		double rexp = Math.exp(re);
		re = Math.cos(im) * rexp;
		im = Math.sin(im) * rexp;
		return this;
	}
	public Complex square()
	{
		double tmp = re;
		re = re*re-im*im;
		im = 2.0*tmp*im;
		return this;
	}
	public Complex cube()
	{
		double re2 = re*re-im*im;
		double im2 = 2.0*re*im;
		double tmp = re;
		re = re*re2 - im2*im;
		im = tmp*im2 + im*re2;
		return this;
	}
	
	public Complex sqrt()
	{
		if (im != 0.0 || re > 0.0)
		{
			re = Math.sqrt((re + Math.hypot(re, im))*0.5);
			im /= 2.0*re;
		}
		else
		{
			im = Math.sqrt(-re);
			re = 0.0;
		}
		return this;
	}
	public Complex cbrt()
	{ /* very lazy */
		double abs = Math.hypot(re, im);
		double arg;
		
		if (re != 0.0 || im != 0.0)
		{
			if (re >= 0.0 || im != 0.0)
				arg = Math.acos(re/abs)*Math.signum(im);
			else
				arg = Math.PI;
		}
		else
			arg = 0.0;
		
		abs = Math.cbrt(abs);
		arg = arg/3.0;
		re = Math.cos(arg) * abs;
		im = Math.sin(arg) * abs;
		return this;
	}
	
	public Complex log()
	{
		double abs = Math.hypot(re, im);
		double tre = Math.log(abs);
		if (re >= 0.0 || im != 0)
			im = Math.acos(re/abs)*Math.signum(im);
		else
			im = Math.PI;
		re = tre;
		return this;
	}
	
	public Complex cos()
	{
		double pexp = Math.exp(im);
		double mexp = 1.0/pexp;
		double tmp = re;
		re = 0.5 * Math.cos(tmp) * (mexp+pexp);
		im = 0.5 * Math.sin(tmp) * (mexp-pexp);
		return this;
	}
	public Complex sin()
	{
		double pexp = Math.exp(im);
		double mexp = 1.0/pexp;
		double tmp = re;
		re = 0.5 * Math.sin(tmp) * (mexp+pexp);
		im = 0.5 * Math.cos(tmp) * (pexp-mexp);
		return this;
	}
	public Complex tan()
	{
		double pexp = Math.exp(im);
		double mexp = 1.0/pexp;
		double c = Math.cos(re);
		double s = Math.sin(re);
		re = s * (mexp+pexp);
		im = c * (pexp-mexp);
		divide(c * (mexp+pexp), s * (mexp-pexp));
		return this;
	}
	public Complex cot()
	{
		double pexp = Math.exp(im);
		double mexp = 1.0/pexp;
		double c = Math.cos(re);
		double s = Math.sin(re);
		re = c * (mexp+pexp);
		im = s * (mexp-pexp);
		divide(s * (mexp+pexp), c * (pexp-mexp));
		return this;
	}
	
	public Complex acos()
	{
		double tre = re;
		double tim = im;
		re = 1.0 - tre*tre + tim*tim;
		im = -2.0 * tre*tim;
		sqrt();
		double tmp = re;
		re = tre - im;
		im = tim + tmp;
		log();
		tmp = re;
		re = im;
		im = -tmp;
		return this;
	}
	public Complex asin()
	{
		double tre = re;
		double tim = im;
		re = 1.0 - tre*tre + tim*tim;
		im = -2.0 * tre*tim;
		sqrt();
		re -= tim;
		im += tre;
		log();
		double tmp = re;
		re = im;
		im = -tmp;
		return this;
	}
	public Complex atan()
	{
		double tre = re;
		double tim = im;
		re = 1.0 - tim;
		im = tre;
		log();
		double lre = re;
		double lim = im;
		re = 1.0 + tim;
		im = -tre;
		log();
		double tmp = re;
		re = 0.5 * (lim-im);
		im = 0.5 * (tmp-lre);
		return this;
	}
	public Complex acot()
	{
		recip();
		double tre = re;
		double tim = im;
		re = 1.0 - tim;
		im = tre;
		log();
		double lre = re;
		double lim = im;
		re = 1.0 + tim;
		im = -tre;
		log();
		double tmp = re;
		re = 0.5 * (lim-im);
		im = 0.5 * (tmp-lre);
		return this;
	}
	
	public Complex cosh()
	{
		double pexp = Math.exp(re);
		double mexp = 1.0/pexp;
		re = 0.5 * Math.cos(im) * (mexp+pexp);
		im = 0.5 * Math.sin(im) * (pexp-mexp);
		return this;
	}
	public Complex sinh()
	{
		double pexp = Math.exp(re);
		double mexp = 1.0/pexp;
		re = 0.5 * Math.cos(im) * (pexp-mexp);
		im = 0.5 * Math.sin(im) * (pexp+mexp);
		return this;
	}
	public Complex tanh()
	{
		double pexp = Math.exp(re);
		double mexp = 1.0/pexp;
		double c = Math.cos(im);
		double s = Math.sin(im);
		re = c * (pexp-mexp);
		im = s * (pexp+mexp);
		divide(c * (mexp+pexp), s * (pexp-mexp));
		return this;
	}
	public Complex coth()
	{
		double pexp = Math.exp(re);
		double mexp = 1.0/pexp;
		double c = Math.cos(im);
		double s = Math.sin(im);
		re = c * (pexp+mexp);
		im = s * (pexp-mexp);
		divide(c * (pexp-mexp), s * (pexp+mexp));
		return this;
	}
	
	public Complex acosh()
	{
		double tre = re;
		double tim = im;
		re = tre*tre - tim*tim - 1.0;
		im = 2.0 * tre*tim;
		sqrt();
		re += tre;
		im += tim;
		log();
		return this;
	}
	public Complex asinh()
	{
		double tre = re;
		double tim = im;
		re = tre*tre - tim*tim + 1.0;
		im = 2.0 * tre*tim;
		sqrt();
		re += tre;
		im += tim;
		log();
		return this;
	}
	public Complex atanh()
	{
		double tre = 1.0 - re;
		double tim = -im;
		re += 1.0;
		divide(tre, tim);
		log();
		re *= 0.5;
		im *= 0.5;
		return this;
	}
	public Complex acoth()
	{
		double tre = re - 1.0;
		double tim = im;
		re += 1.0;
		divide(tre, tim);
		log();
		re *= 0.5;
		im *= 0.5;
		return this;
	}
	
	/* transformations */
	public Complex transform(double[] m)
	{
		double tre = re;
		re = m[0]*re + m[1]*im + m[2];
		im = m[3]*tre + m[4]*im + m[5];
		return this;
	}
	public Complex transform(Matrix3D m)
	{
		double tre = re;
		re = m.m[0]*re + m.m[1]*im + m.m[2];
		im = m.m[3]*tre + m.m[4]*im + m.m[5];
		return this;
	}
	
	/* Object method overloading */
	public boolean equals(Object ob)
	{
		if (ob instanceof Complex)
			return (re == ((Complex)ob).re) && (im == ((Complex)ob).im); 
		else
			return false;
	}
	
	public String toString()
	{
		return "(" + re + "+" + im + "I)";
	}

	public Node createNode(Document doc)
    {
		Element elem = doc.createElement("complex");
		elem.setTextContent(String.format(Locale.ENGLISH, "%15.16g %15.16g", re, im));
	    return (Node)elem;
    }
	
	public void getFromNode(Node node) throws DOMNodeException
    {
		Element elem = (Element)node;
		Scanner scanner = new Scanner(elem.getTextContent());
		scanner.useLocale(Locale.ENGLISH);
		
		if (elem.getAttributes().getLength() != 0)
			throw new DOMNodeBadStructureException("Unexpected attributes at Complex");
		
		try
		{
			double r = scanner.nextDouble();
    		double i = scanner.nextDouble();
    		if (scanner.hasNext())
    			throw new DOMNodeParseException("Exception at parsing Complex");
    		re = r;
    		im = i;
		}
		catch(InputMismatchException e)
		{ throw new DOMNodeParseException("Exception at parsing Complex"); }
		catch(NoSuchElementException e)
		{ throw new DOMNodeParseException("Exception at parsing Complex"); }
    }
	
	public boolean isValidNode(Node node)
    {
	    return (node.getNodeType() == Node.ELEMENT_NODE &&
	    		node.getNodeName() == "complex");
    }
}
