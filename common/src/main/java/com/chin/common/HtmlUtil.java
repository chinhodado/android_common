package com.chin.common;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Utilities for working with HTML
 *
 * Created by Chin on 12-Feb-17.
 */
public class HtmlUtil {
    public static void removeSupTag(Element elem) {
        Elements sups = elem.getElementsByTag("sup");
        for (Element e : sups) {
            e.remove();
        }
    }

    /**
     * Given a link to a wikia image (scaled or not scaled), return a (new) scaled version with the specified width
     * @param link The link to the original scaled wikia image
     * @param newWidth The width of the new image
     * @return The link to the new scaled image
     */
    public static String getScaledWikiaImageLink (String link, int newWidth) {
        // trim off the craps at the end
        if (link.contains(".png")) {
            link = link.substring(0, link.indexOf(".png") + 4);
        }
        else if (link.contains(".jpg")) {
            link = link.substring(0, link.indexOf(".jpg") + 4);
        }
        else {
            return link; // hey, unknown format or something
        }

        // TODO: maybe we should use regex...
        int lastSlash = link.lastIndexOf("/");
        String scaledName = link.substring(lastSlash + 1); // the original scaled image name

        // get the original image name
        int prefixIndex = scaledName.indexOf("px-");
        String originalName = scaledName;
        if (prefixIndex != -1) {
            // this is a scaled link
            int firstOriginalImageNamePosition = scaledName.indexOf("px-") + 3;
            originalName = scaledName.substring(firstOriginalImageNamePosition);
        }

        // the new scaled image
        String newScaledName = newWidth + "px-" + originalName;

        // original image link with the slash
        String originalLink = (prefixIndex == -1)? (link + "/") : link.substring(0, lastSlash + 1);

        // complete new link
        String newScaledLink = originalLink + newScaledName;
        if (prefixIndex == -1) {
            // some additional work to turn a normal image to a thumb/scaled one
            newScaledLink = newScaledLink.replace("/images/", "/images/thumb/");
        }
        return newScaledLink;
    }

    public static String getFullImageLink(String shortenedLink) {
        return "http://vignette" + shortenedLink.charAt(0) + ".wikia.nocookie.net/yugioh/images/" +
                shortenedLink.charAt(1) + "/" + shortenedLink.charAt(1) + shortenedLink.charAt(2) +
                "/" + shortenedLink.substring(3);
    }
}
