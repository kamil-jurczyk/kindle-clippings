package pl.kamiljurczyk.clippings.clippingFile;

public class ClippingFileNotFoundException extends RuntimeException {
    public ClippingFileNotFoundException() {
        super("This file with clippings from Kindle was not found.");
    }
}
