package org.beanfabrics.swing.customizer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.UndeclaredThrowableException;
/**
 * The FileLog is a poor mans logging utility for writing log messages to a file.
 * 
 * @author Michael Karneim
 */
public class FileLog {    
    private final File file;
    private String messagePrefix = "";
    
    public FileLog(File file) {
        this.file = file;
    }

    public FileLog(File file, String messagePrefix) {
        this.file = file;
        this.messagePrefix = messagePrefix;
    }
        
    public File getFile() {
		return file;
	}

	public String getMessagePrefix() {
		return messagePrefix;
	}

	public void setMessagePrefix(String messagePrefix) {
		this.messagePrefix = messagePrefix;
	}

	public void log(String message) {        
        try {
            Writer writer = new FileWriter(file, true);
            writer.write(messagePrefix);
            writer.write(" ");
            writer.write(message);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }        
    }
    
    
}
