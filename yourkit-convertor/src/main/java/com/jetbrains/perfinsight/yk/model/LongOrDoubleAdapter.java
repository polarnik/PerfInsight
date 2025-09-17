package com.jetbrains.perfinsight.yk.model;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter that accepts either integer (e.g., "123") or decimal (e.g., "123.45")
 * text for XML attributes mapped to Long. Decimals are truncated via Double.longValue().
 */
public class LongOrDoubleAdapter extends XmlAdapter<String, Long> {
    @Override
    public Long unmarshal(String v) throws Exception {
        if (v == null || v.isEmpty()) return null;
        try {
            return Long.parseLong(v);
        } catch (NumberFormatException ex) {
            Double d = Double.parseDouble(v);
            return d.longValue();
        }
    }

    @Override
    public String marshal(Long v) throws Exception {
        return v == null ? null : v.toString();
    }
}
