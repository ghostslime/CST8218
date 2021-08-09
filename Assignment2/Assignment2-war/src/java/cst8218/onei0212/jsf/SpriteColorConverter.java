/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cst8218.onei0212.jsf;

import java.awt.Color;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;

/**
 * Converter class for the Color attribute of the Sprite class.
 * 
 * @author  Anwis O'Neill
 * @version 0.1
 * @since   1.8
 * @see     Sprite
 */
@FacesConverter(forClass=Color.class)
public class SpriteColorConverter implements Converter {
    private static final Pattern RGBPattern = 
            Pattern.compile(
                    "\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*"
            );
    
    private static final int NUM_COLOR_COMPONENTS = 3;
    
    /**
     * Converts the string representation of an RGB color object 
     * to a java Color object.
     * 
     * @param context
     * @param component UI component of concern
     * @param value String representation of an RGB color.
     * @return Java Color object.
     * @throws ConverterException if the string could not be parsed 
     *                            to a valid Color object.
     */
    @Override
    public Object getAsObject(FacesContext context, 
            UIComponent component, 
            String value) {
        Matcher RGBFormatValidator = RGBPattern.matcher(value);
        int[] RGBValues = new int[NUM_COLOR_COMPONENTS];
        String[] RGBStringVals;
        int colorIndex = 0;
        int RGBValue = 0;
        Color pickedColor = null;
        
        /* RGB values */ 
        /* Must have 2 commas */
        /* Each comma must have a valid number to its left and right */
        if (RGBFormatValidator.matches()) {
            RGBStringVals = value.split(",");
            for (String RGBStringVal : RGBStringVals) {
                RGBValue = Integer.parseInt(RGBStringVal);
                if (RGBValue < 0 || RGBValue > 255) {
                    FacesMessage errorMessage = new FacesMessage(
                            "RGB value(s) out of bounds");
                    throw new ConverterException(errorMessage);
                }
                RGBValues[colorIndex++] = RGBValue;
            }
            
            pickedColor = new Color(RGBValues[0], RGBValues[1], RGBValues[2]);
        } else {
            FacesMessage errorMessage = 
                    new FacesMessage("Invalid RGB value (RRR, GGG, BBB)");
            throw new ConverterException(errorMessage);
        }
        
        return pickedColor;
    }
    
    /**
     * Breaks down the Color object as a simple string representation
     * of the RGB values.
     * 
     * @param context context.
     * @param component UI component of concern
     * @param value Java Color object.
     * @return RGB values string.
     * @throws ConverterException if the object is not a Color object.
     */
    @Override
    public String getAsString(FacesContext context,
            UIComponent component,
            Object value) {
        String colorStr = "RRR, GGG, BBB";
        
        if (null == value) {
            FacesMessage errorMessage = 
                    new FacesMessage("Object is null");
            throw new ConverterException(errorMessage);
        } else if (!(value instanceof Color)) {
            FacesMessage errorMessage = 
                    new FacesMessage("Object is not a Color");
            throw new ConverterException(errorMessage);
        } else {
            Color color = (Color) value;
            colorStr = String.format("%d,%d,%d", color.getRed(),
                                                 color.getGreen(),
                                                 color.getBlue());
        }
        
        return colorStr;
    }
}
