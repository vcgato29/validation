/**
 * MappingConsole.java
 *
 * This file was generated by MapForce 2017sp2.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the MapForce Documentation for further details.
 * http://www.altova.com/mapforce
 */


package com.mapforce;

import com.altova.types.*;


public class MappingConsole {

	public static void main(String[] args) {
		System.err.println("Mapping Application");

		java.util.Hashtable<String,String>	mapArguments = new java.util.Hashtable<String,String>();
		if( args.length <= 1 )	{
			System.err.println();
			System.err.println( "WARNING: No parameters given!");
			System.err.println( "SYNTAX: java com.mapforce.MappingConsole [/input ...] ");
			System.err.println( "SYNTAX: ant -Dcmdline=\"[/input ...] \"");
			System.err.println( "Note: If you want to use spaces as values write them in-between double quotes." );
			System.err.println();
		}
		else
		{
			for( int i = 0; i < args.length; i++ )
			{
				String sName = args[ i ];
				if( sName.substring( 0, 1 ).equals( "/" )  &&  ( i + 1 ) < args.length )
					mapArguments.put( sName.substring( 1, sName.length() ), args[ ++i ] );
			}
		}

		try { // Mapping
			TraceTargetConsole ttc = new TraceTargetConsole();


			MappingMapToINVOIC_D14B_ISO20625 MappingMapToINVOIC_D14B_ISO20625Object = new MappingMapToINVOIC_D14B_ISO20625();




			MappingMapToINVOIC_D14B_ISO20625Object.registerTraceTarget(ttc);
	

			// run mapping
			//
			// you have different options to provide mapping input and output:
			//
			// files using file names (available for XML, text, and Excel):
			//   com.altova.io.FileInput(String filename)
			//   com.altova.io.FileOutput(String filename)
			//
			// streams (available for XML, text, and Excel):
			//   com.altova.io.StreamInput(java.io.InputStream stream)
			//   com.altova.io.StreamOutput(java.io.OutputStream stream)
			//
			// strings (available for XML and text):
			//   com.altova.io.StringInput(String xmlcontent)
			//   com.altova.io.StringOutput()	(call getContent() after run() to get StringBuffer with content)
			//
			// Java IO reader/writer (available for XML and text):
			//   com.altova.io.ReaderInput(java.io.Reader reader)
			//   com.altova.io.WriterOutput(java.io.Writer writer)
			//
			// DOM documents (for XML only):
			//   com.altova.io.DocumentInput(org.w3c.dom.Document document)
			//   com.altova.io.DocumentOutput(org.w3c.dom.Document document)
			// 
			// By default, run will close all inputs and outputs. If you do not want this,
			// call the following function:
			// MappingMapToINVOIC_D14B_ISO20625Object.setCloseObjectsAfterRun(false);

			{

				MappingMapToINVOIC_D14B_ISO20625Object.run(
						( mapArguments.containsKey("input") ? com.altova.CoreTypes.castToString(mapArguments.get("input")): null ));


			}



			System.err.println("Finished");
		} 
		catch (com.altova.UserException ue) 
		{
			System.err.print("USER EXCEPTION:");
			System.err.println( ue.getMessage() );
			System.exit(1);
		}
		catch (com.altova.AltovaException e)
		{
			System.err.print("ERROR: ");
			System.err.println( e.getMessage() );
			if (e.getInnerException() != null)
			{
				System.err.print("Inner exception: ");
				System.err.println(e.getInnerException().getMessage());
				if (e.getInnerException().getCause() != null)
				{
					System.err.print("Cause: ");
					System.err.println(e.getInnerException().getCause().getMessage());
				}
			}
			System.err.println("\nStack Trace: ");
			e.printStackTrace();
			System.exit(1);
		}
		
		catch (Exception e) {
			System.err.print("ERROR: ");
			System.err.println( e.getMessage() );
			System.err.println("\nStack Trace: ");
			e.printStackTrace();
			System.exit(1);
		}

	}
}


class TraceTargetConsole implements com.altova.TraceTarget {
	public void writeTrace(String info) {
		System.err.println(info);
	}
}
