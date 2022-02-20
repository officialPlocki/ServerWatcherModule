package me.refluxo.module.util;

import javax.net.ssl.HttpsURLConnection;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class used to execute Discord Webhooks with low effort
 */
public class DiscordHook {

    // A private final field that holds the webhook URL.
    private final String url;
    // It's a field that holds the content of the message.
    private String content;
    // It's a field that holds the username of the webhook.
    private String username;
    // It's a field that holds the avatar URL of the webhook.
    private String avatarUrl;
    // It's a field that holds the TTS value.
    private boolean tts;
    // This is a field that holds the embed objects.
    private final List<EmbedObject> embeds = new ArrayList<>();

    /**
     * Constructs a new DiscordWebhook instance
     *
     * @param url The webhook URL obtained in Discord
     */
    public DiscordHook(String url) {
        this.url = url;
    }

    /**
     * It sets the content of the question.
     *
     * @param content The content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * It sets the username of the user.
     *
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * It sets the avatarUrl variable to the value of the avatarUrl parameter.
     *
     * @param avatarUrl The URL of the user's avatar image.
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * It sets the tts variable to true or false.
     *
     * @param tts true if the message is a text-to-speech message, false if it is a text message.
     */
    public void setTts(boolean tts) {
        this.tts = tts;
    }

    /**
     * Adds an embed object to the embeds list
     *
     * @param embed The embed object to add to the embed list.
     */
    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    /**
     * It takes the JSON object and sends it to the webhook URL
     */
    public void execute() throws IOException {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            List<JSONObject> embedObjects = new ArrayList<>();

            for (EmbedObject embed : this.embeds) {
                JSONObject jsonEmbed = new JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                if (embed.getColor() != null) {
                    Color color = embed.getColor();
                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();

                    jsonEmbed.put("color", rgb);
                }

                EmbedObject.Footer footer = embed.getFooter();
                EmbedObject.Image image = embed.getImage();
                EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                EmbedObject.Author author = embed.getAuthor();
                List<EmbedObject.Field> fields = embed.getFields();

                if (footer != null) {
                    JSONObject jsonFooter = new JSONObject();

                    jsonFooter.put("text", footer.getText());
                    jsonFooter.put("icon_url", footer.getIconUrl());
                    jsonEmbed.put("footer", jsonFooter);
                }

                if (image != null) {
                    JSONObject jsonImage = new JSONObject();

                    jsonImage.put("url", image.getUrl());
                    jsonEmbed.put("image", jsonImage);
                }

                if (thumbnail != null) {
                    JSONObject jsonThumbnail = new JSONObject();

                    jsonThumbnail.put("url", thumbnail.getUrl());
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }

                if (author != null) {
                    JSONObject jsonAuthor = new JSONObject();

                    jsonAuthor.put("name", author.getName());
                    jsonAuthor.put("url", author.getUrl());
                    jsonAuthor.put("icon_url", author.getIconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                }

                List<JSONObject> jsonFields = new ArrayList<>();
                for (EmbedObject.Field field : fields) {
                    JSONObject jsonField = new JSONObject();

                    jsonField.put("name", field.getName());
                    jsonField.put("value", field.getValue());
                    jsonField.put("inline", field.isInline());

                    jsonFields.add(jsonField);
                }

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close(); //I'm not sure why but it doesn't work without getting the InputStream
        connection.disconnect();
    }

    /**
     * EmbedObject is a class that contains all the information needed to create an embed
     */
    public static class EmbedObject {
        // Creating a new instance of the class and initializing the instance variables.
        private String title;
        // Creating a new instance of the class and initializing the instance variables.
        private String description;
        // Creating a new instance of the class and setting the url to the value of the argument.
        private String url;
        // Creating a new instance of the Color class.
        private Color color;

        // Creating a footer object and setting the footer text to "This is the footer"
        private Footer footer;
        // Creating a new Thumbnail object and setting the thumbnail property to the value of the thumbnail parameter.
        private Thumbnail thumbnail;
        // Creating an image object and setting it to the image variable.
        private Image image;
        // Creating a new Author object and setting the author variable to that object.
        private Author author;
        //
        private List<Field> fields = new ArrayList<>();

        /**
         * It returns the title of the book.
         *
         * @return The title of the question.
         */
        public String getTitle() {
            return title;
        }

        /**
         * It returns the description of the object.
         *
         * @return The description of the question.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns the URL of the page
         *
         * @return The url variable.
         */
        public String getUrl() {
            return url;
        }

        /**
         * Returns the color of the object
         *
         * @return The color of the circle.
         */
        public Color getColor() {
            return color;
        }

        /**
         * Returns the footer of the page
         *
         * @return The footer object.
         */
        public Footer getFooter() {
            return footer;
        }

        /**
         * Returns the thumbnail of the image
         *
         * @return The thumbnail object.
         */
        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        /**
         * Returns the image of the icon
         *
         * @return The image object.
         */
        public Image getImage() {
            return image;
        }

        /**
         * Get the author of the book
         *
         * @return The author of the book.
         */
        public Author getAuthor() {
            return author;
        }

        /**
         * Returns a list of fields in the class
         *
         * @return A list of Field objects.
         */
        public List<Field> getFields() {
            return fields;
        }

        /**
         * It sets the title of the embed object.
         *
         * @param title The title of the embed object.
         * @return Nothing.
         */
        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * It sets the description of the embed object.
         *
         * @param description The description of the embed.
         * @return Nothing.
         */
        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * This function sets the url of the embed object
         *
         * @param url The URL of the embed object.
         * @return Nothing.
         */
        public EmbedObject setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * This function sets the color of the embed object
         *
         * @param color The color of the embed.
         * @return Nothing.
         */
        public EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        /**
         * This function sets the footer of the embed object
         *
         * @param text The text of the footer.
         * @param icon The icon URL for the footer.
         * @return Nothing.
         */
        public EmbedObject setFooter(String text, String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        /**
         * This function sets the thumbnail of the embed object
         *
         * @param url The URL of the thumbnail.
         * @return Nothing.
         */
        public EmbedObject setThumbnail(String url) {
            this.thumbnail = new Thumbnail(url);
            return this;
        }

        /**
         * This function sets the image of the embed object
         *
         * @param url The url of the image.
         * @return Nothing.
         */
        public EmbedObject setImage(String url) {
            this.image = new Image(url);
            return this;
        }

        /**
         * This function sets the author of the embed object
         *
         * @param name The name of the author.
         * @param url The url of the author.
         * @param icon The url of the icon to use for the author.
         * @return Nothing.
         */
        public EmbedObject setAuthor(String name, String url, String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        /**
         * Adds a field to the embed object
         *
         * @param name The name of the field.
         * @param value The value of the field.
         * @param inline Whether or not the field should be inline.
         * @return Nothing.
         */
        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        /**
         * The Footer class is a class that contains a String variable called text and a String variable called iconUrl
         */
        private class Footer {
            // Creating a class called Question with a constructor that takes a String as a parameter.
            private String text;
            // Creating a new instance of the Question class and setting the question, answer, and category.
            private String iconUrl;

            // Creating a new object of type Footer.
            private Footer(String text, String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }

            /**
             * It returns the text of the question.
             *
             * @return The text of the question.
             */
            private String getText() {
                return text;
            }

            /**
             * Returns the icon URL for the given icon name
             *
             * @return The iconUrl variable.
             */
            private String getIconUrl() {
                return iconUrl;
            }
        }

        /**
         * A Thumbnail is a URL
         */
        private class Thumbnail {
            // Creating a new instance of the class and setting the url to the value of the argument.
            private String url;

            // Creating a new object of type Thumbnail.
            private Thumbnail(String url) {
                this.url = url;
            }

            /**
             * It returns the url.
             *
             * @return The url variable.
             */
            private String getUrl() {
                return url;
            }
        }

        /**
         * An Image is a URL
         */
        private class Image {
            // Creating a new instance of the class and setting the url to the value of the argument.
            private String url;

            // Creating a new Image object and setting the url to the url passed in.
            private Image(String url) {
                this.url = url;
            }

            /**
             * It returns the url.
             *
             * @return The url variable.
             */
            private String getUrl() {
                return url;
            }
        }

        /**
         * This class creates a new object called "Author" and creates a variable called "name", "url", and "iconUrl"
         */
        private class Author {
            // Creating a class called "Person" and creating a variable called "name"
            private String name;
            // Creating a new instance of the class and setting the url to the value of the argument.
            private String url;
            // Creating a new instance of the Question class and setting the question, answer, and category.
            private String iconUrl;

            // Creating a new Author object.
            private Author(String name, String url, String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }

            /**
             * It returns the name of the person.
             *
             * @return The name of the object.
             */
            private String getName() {
                return name;
            }

            /**
             * It returns the url.
             *
             * @return The url variable.
             */
            private String getUrl() {
                return url;
            }

            /**
             * Returns the icon URL for the given icon name
             *
             * @return The iconUrl variable.
             */
            private String getIconUrl() {
                return iconUrl;
            }
        }

        /**
         * This class is used to store the name and value of a field
         */
        private class Field {
            // Creating a class called "Person" and creating a variable called "name"
            private String name;
            // Creating a class called "Question" and it is creating a variable called "value"
            private String value;
            // Checking if the inline variable is true.
            private boolean inline;

            // This is the constructor for the Field class.
            private Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            /**
             * It returns the name of the person.
             *
             * @return The name of the object.
             */
            private String getName() {
                return name;
            }

            /**
             * It returns the value of the variable value.
             *
             * @return The value of the field.
             */
            private String getValue() {
                return value;
            }

            /**
             * Returns true if the current node is inline
             *
             * @return A boolean value.
             */
            private boolean isInline() {
                return inline;
            }
        }
    }

    /**
     * This class is used to convert a JSONObject into a string
     */
    private class JSONObject {

        //
        private final HashMap<String, Object> map = new HashMap<>();

        /**
         * Put a key/value pair into the map
         *
         * @param key The key to be used to store the value.
         * @param value The value to be stored in the map.
         */
        void put(String key, Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }

        /**
         * This function is used to convert a JSONObject into a string
         *
         * @return The string representation of the map.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String) {
                    builder.append(quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof JSONObject) {
                    builder.append(val.toString());
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(val);
                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }

                builder.append(++i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        /**
         * Given a string, return a string that is the string surrounded by double quotes
         *
         * @param string The string to quote.
         * @return The string "\"" + string + "\""
         */
        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }

}