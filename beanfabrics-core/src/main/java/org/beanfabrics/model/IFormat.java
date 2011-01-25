package org.beanfabrics.model;

/**
 * The {@link IFormat} defines the programming interface for formatting
 * instances of a specific type into Strings and for parsing Strings back into
 * objects.
 * 
 * @author Michael Karneim
 * @param <T>
 */
public interface IFormat<T> {
    /**
     * Parses the given text and returns an instance of T that is represented by
     * the text.
     * 
     * @param text
     * @return an instance of T that represents the given text
     * @throws ConversionException
     */
    T parse(String text)
        throws ConversionException;

    /**
     * Formats the given value to a String representation.
     * 
     * @param value
     * @return the String representation of the given value
     */
    String format(T value);

}
