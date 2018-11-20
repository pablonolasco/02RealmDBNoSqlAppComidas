package mx.com.firebase.yanabit.pablonolasco.appcomidas.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Comida> ITEMS = new ArrayList<Comida>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Comida> ITEM_MAP = new HashMap<String, Comida>();

    private static final int COUNT = 0;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    /**
     * TODO metodo para actualizar el objeto
     * @param comida
     */
    public static void update(Comida comida){
        try {
            // TODO: 19/11/18 busca el elemento por el indice y objeto y lo reemplaza
            ITEMS.set(ITEMS.indexOf(comida),comida);
            // TODO: 19/11/18 hace la misma accion en el diccionario
            ITEM_MAP.put(comida.getId(),comida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void delete(Comida comida){
        try {
            ITEMS.remove(comida);
            ITEM_MAP.remove(comida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addItem(Comida item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Comida createDummyItem(int position) {
        return new Comida(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Comida {
        public  String id;
        public  String nombre;
        public  String precio;

        public Comida(){}
        public Comida(String nombre, String precio) {
            this.nombre = nombre;
            this.precio = precio;
        }

        public Comida(String id, String nombre, String precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getPrecio() {
            return precio;
        }

        public void setPrecio(String precio) {
            this.precio = precio;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}
