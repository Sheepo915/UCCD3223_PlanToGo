package com.utar.plantogo.ui.attraction;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * <h3>AttractionComponentFactory</h3>
 * <p>This is to signifies which AttractionComponent to be initialized</p>
 * Why?
 * <ul>
 *     <li>Encapsulation of Object Creation</li>
 *     <li>Flexible and Scalable Object</li>
 *     <li>Consistent Object Creation</li>
 *     <li>Improve Readability and Maintainability</li>
 * </ul>
 */
public class AttractionComponentFactory {
    /**
     * Function to create an component based on the retrieved content from <i>Tripadvisor API</i>
     * @param context Current application context
     * @param type Type of the AttractionComponent that will be generated
     * @param retrievedInformation Expected output from <i>Tripadvisor API</i>
     * @return Either AttractionCarouselComponent or AttractionWithDescriptionComponent
     */
    @NonNull
    public static AttractionComponent createAttractionComponent(Context context, @NonNull AttractionType type, Map<String, Objects> retrievedInformation) {
        AttractionComponent component;

        switch (type) {
            case LIST_ITEM:
                component = new AttractionListComponent(context);
                break;
            case CAROUSEL_ITEM:
                component = new AttractionCarouselComponent(context);
                break;
            default:
                throw new IllegalArgumentException("Unknown AttractionType: " + type);
        }

        return component;
    }

    /**
     * Enum for defining which type of AttractionComponent to be initialized
     */
    public enum AttractionType {
        LIST_ITEM, CAROUSEL_ITEM
    }
}
