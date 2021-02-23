package app.model;




public class Image {
    private String folder;
    private String name;



    public Image(int id, boolean person) {
        folder = (person) ? "/img/people/" : "/img/shows/";
        name = id + ".jpg";
    }

    public String getPath(){
        return String.format("%s%s", folder, name);
    }


}
