package com.jetbrains.perfinsight.yk.model;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JAXB adapter that parses doubles from strings that may contain non-numeric prefixes like "< 0.1".
 * Extracts the first decimal number occurrence and parses it, otherwise returns null.
 */
public class DoubleLenientAdapter extends XmlAdapter<String, Double> {
    private static final Pattern NUM_PATTERN = Pattern.compile("[-+]?\\d+(?:\\.\\d+)?");

    @Override
    public Double unmarshal(String v) throws Exception {
        if (v == null) return null;
        String s = v.trim();
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ignored) {
            Matcher m = NUM_PATTERN.matcher(s);
            if (m.find()) {
                return Double.parseDouble(m.group());
            }
            return null;
        }
    }

    @Override
    public String marshal(Double v) throws Exception {
        return v == null ? null : v.toString();
    }
}
