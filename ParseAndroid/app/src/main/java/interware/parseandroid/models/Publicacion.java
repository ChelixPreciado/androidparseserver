package interware.parseandroid.models;

/**
 * Created by chelixpreciado on 7/21/16.
 */
public class Publicacion {

    private String descripcion;
    private String imageUrl;

    public Publicacion(){}

    public Publicacion(String descripcion, String imageIrl) {
        this.descripcion = descripcion;
        this.imageUrl = imageIrl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
