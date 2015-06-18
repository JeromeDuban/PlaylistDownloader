package fr.jeromeduban.playlistdownloader;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by Jérôme on 11/06/2015 .
 */
@JsonObject
public class Image {

    /*
     * Standard field declaration.
     */
    @JsonField
    public String format;

    /*
     * Note: passing the name parameter into @JsonField will cause
     * LoganSquare to use "_id" in JSON parsing and processing instead
     * of "imageId".
     */
    @JsonField(name = "_id")
    public int imageId;

    @JsonField
    public String url;

    @JsonField
    public String description;

    /*
     * Note that even though this is a package-local field,
     * LoganSquare can parse and serialize it without issue.
     */
    @JsonField(name = "similar_images")
    List<Image> similarImages;





}

