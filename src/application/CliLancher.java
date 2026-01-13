package application;

import java.io.File;
import java.io.IOException;

import algorithms.HuffmanCompress;
import algorithms.HuffmanDecompress;
import javafx.scene.control.Alert;

public class CliLancher {

	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Usge: Compress / Decompresss");
		}
		String action = args[0];
		File in = new File(args[1]);
		
		HuffmanCompress comp = new HuffmanCompress();
		HuffmanDecompress decomp = new HuffmanDecompress();
		if(action.equalsIgnoreCase("compress")) {
		    try {
		        File out = new File(in.getParentFile(), in.getName() + ".huff");
		        comp.compress(in); // ← يجب تعديل Compress ليأخذ ملف الإخراج
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		} else if(action.equalsIgnoreCase("decompress")) {
		    try {
		        String origName = in.getName();
		        if(origName.endsWith(".huff")) {
		            origName = origName.substring(0, origName.length() - 5);
		        }
		        File out = new File(in.getParentFile(), origName);
		        decomp.decompress(in); // ← يجب تعديل Decompress ليأخذ ملف الإخراج
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}

	}



}
