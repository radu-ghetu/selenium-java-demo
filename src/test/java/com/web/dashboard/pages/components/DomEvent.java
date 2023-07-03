package com.web.dashboard.pages.components;

/**
 *
 */
public enum DomEvent {
    CHANGE,
    BLUR,
    FOCUS;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}