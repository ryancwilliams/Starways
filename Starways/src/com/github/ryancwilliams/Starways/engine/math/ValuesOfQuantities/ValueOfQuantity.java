/*
 * Copyright 2013 ryancwilliams.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ryancwilliams.Starways.engine.math.ValuesOfQuantities;

import java.io.Serializable;
import java.text.DecimalFormat;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * An class providing common methods for working with a ValueOfQuantity object.
 *
 * @author ryancwilliams
 */
@Embeddable
public class ValueOfQuantity<QUANTITY_TYPE extends Enum & Quantity> implements
        Comparable<ValueOfQuantity<QUANTITY_TYPE>>, Serializable {

    /**
     * Determines if a de-serialized file is compatible with this class.
     *
     * Maintainers must change this value if and only if the new version of this
     * class is not compatible with old versions. See Sun docs for <a
     * href=http://java.sun.com/products/jdk/1.1/docs/guide/serialization/spec/
     * version.doc.html>
     * details</a>.
     */
    private static final long serialVersionUID = -4430113729107419959L;
    /**
     * the value in the specified quantity.
     */
    protected double val;
    /**
     * the quantity the value is specified in.
     */
    @Enumerated(EnumType.STRING)
    private QUANTITY_TYPE quantity;

    /**
     * Creates a ValueOfQuantity object.
     *
     * @param value the value in the specified quantity.
     * @param quantity the quantity the value is specified in.
     */
    public ValueOfQuantity(double value, QUANTITY_TYPE quantity) {
        this.val = value;
        this.quantity = quantity;
    }

    /**
     * Creates a ValueOfQuantity object with a value of 0 and a Quantity of
     * <code>null</code>
     */
    protected ValueOfQuantity() {
        this(0, null);
    }

    /**
     * Gets the value in the specified quantity.
     *
     * @return the value in the specified quantity.
     */
    public double getValue() {
        return val;
    }

    /**
     * Sets the value in the specified quantity.
     *
     * @param value the value to set specified quantity into.
     */
    public void setValue(double value) {
        this.val = value;
    }

    /**
     * Gets the quantity the value is specified in.
     *
     * @return the quantity the value is specified in.
     */
    public QUANTITY_TYPE getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the quantity the value is specified in.
     *
     * @param quantity the quantity to change the value specified into.
     */
    public void setQuantity(QUANTITY_TYPE quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the value in the specified quantity.
     *
     * @param quantity the quantity to return the value in.
     * @return the value in the specified quantity.
     */
    public double getConvertedValue(QUANTITY_TYPE quantity) {
        return this.val
                * (this.quantity.getConversionFactor() / quantity.getConversionFactor());
    }

    /**
     * Gets a ValueOfQuantity in the specified quantity.
     *
     * @param quantity the quantity the value should be in
     * @return the ValueOfQuantity in the specified quantity.
     */
    public ValueOfQuantity<QUANTITY_TYPE> getConvertedValueOfQuantity(QUANTITY_TYPE quantity) {
        return new ValueOfQuantity(this.getConvertedValue(quantity), quantity);
    }

    /**
     * Converts this ValueOfQuantity to the specified quantity.
     *
     * @param quantity the quantity this value should be in.
     */
    public void convertTo(QUANTITY_TYPE quantity) {
        this.val = this.getConvertedValue(quantity);
        this.quantity = quantity;
    }

    /**
     * Gets the value of this ValueOfQuantity in its base units.
     *
     * @return the value of this ValueOfQuantity in its base units.
     */
    public double getBaseValue() {
        return this.val * this.quantity.getConversionFactor();
    }

    /**
     * Gets this ValueOfQuantity as a formated string.
     *
     * @param precision the number decimals to create the value to.
     * @return the ValueOfQuantity as a formated string.
     */
    public String asString(int precision) {
        //Call the formator to format this value.
        return this.formatValueOfQuantity(precision);
    }

    /**
     * Gets this ValueOfQuantity as a formated string using default precision.
     *
     * @return the ValueOfQuantity as a formated string.
     */
    public String asString() {
        return this.asString(2);
    }

    /**
     * Gets a DecimalFormat that can be used to create decimal strings with the
     * specified number of decimal places.
     *
     * @param decimalPlaces the number of decimal places to generate a
     * DecimalFormat for.
     * @return The DecimalFormat.
     */
    private static DecimalFormat getFormat(int decimalPlaces) {
        //Create a digit and a decimal point with leading digits and thousands separator 
        String pattern = "###,##0.";

        //For <decimalPlaces> of times
        for (int i = 0; i < decimalPlaces; i++) {
            //add a digit
            pattern += "0";
        }

        //Create the Decimal format and return it
        return new DecimalFormat(pattern);
    }

    /**
     * Creates a formated string for this ValueOfQuantity. This method is called
     * by asString methods in ValueOfQuantity. That asString method is preferred
     * over this method.
     *
     * @return a formated string based on the provided ValueOfQuantity.
     */
    private String formatValueOfQuantity(int decimalPlaces) {
        //Create a empty soutput tring 
        String formatedString = "";

        //Add the formated number to the string
        formatedString += ValueOfQuantity.getFormat(decimalPlaces)
                .format(this.getValue());

        //Add a space to the string
        formatedString += " ";

        //Add the quantity symbol to the string
        formatedString += this.getQuantity().getSymbol();

        //return the formated string
        return formatedString;
    }

    @Override
    public int compareTo(ValueOfQuantity<QUANTITY_TYPE> that) {
        //Convert values to base unit
        double baseValueThis = this.getBaseValue();
        double baseValueThat = that.getBaseValue();

        //Caculate Delta
        double delta = baseValueThis - baseValueThat;

        //Create a output interger
        int out;
        if (delta > Integer.MAX_VALUE) {
            out = Integer.MAX_VALUE;
        } else if (delta < Integer.MIN_VALUE) {
            out = Integer.MIN_VALUE;
        } else {
            out = (int) delta;
        }

        //Return output
        return out;
    }

    @Override
    public String toString() {
        return "ValueOfQuantity{" + "value=" + val + ", quantity=" + quantity + '}';
    }
}
