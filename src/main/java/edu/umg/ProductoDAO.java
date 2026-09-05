package edu.umg;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class ProductoDAO implements AutoCloseable {

    private final MongoClient cliente;
    private final MongoCollection<Document> productos;

    public ProductoDAO() {
        cliente = ConexionMongo.conectar();
        MongoDatabase db = cliente.getDatabase("tienda");
        productos = db.getCollection("productos");
    }

    public void insertar(Producto producto) {
        Document doc = new Document("codigo", producto.getCodigo())
                .append("nombre", producto.getNombre())
                .append("categoria", producto.getCategoria())
                .append("precio", producto.getPrecio())
                .append("existencia", producto.getExistencia());
        
        productos.insertOne(doc);
        System.out.println("Producto insertado con éxito.");
    }

    public void listar() {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        for (Document doc : productos.find()) {
            System.out.printf("Código: %s | Nombre: %s | Categoría: %s | Precio: Q%.2f | Existencia: %d\n",
                    doc.getString("codigo"),
                    doc.getString("nombre"),
                    doc.getString("categoria"),
                    doc.getDouble("precio"),
                    doc.getInteger("existencia"));
        }
    }

    public Document buscarPorCodigo(String codigo) {
        Document doc = productos.find(Filters.eq("codigo", codigo)).first();
        if (doc != null) {
            System.out.println("\n--- PRODUCTO ENCONTRADO ---");
            System.out.printf("Código: %s | Nombre: %s | Categoría: %s | Precio: Q%.2f | Existencia: %d\n",
                    doc.getString("codigo"),
                    doc.getString("nombre"),
                    doc.getString("categoria"),
                    doc.getDouble("precio"),
                    doc.getInteger("existencia"));
        } else {
            System.out.println("Producto no encontrado.");
        }
        return doc;
    }

    public void actualizarExistencia(String codigo, int nuevaExistencia) {
        productos.updateOne(
            Filters.eq("codigo", codigo),
            Updates.set("existencia", nuevaExistencia)
        );
        System.out.println("Existencia actualizada con éxito.");
    }

    public void actualizarPrecio(String codigo, double nuevoPrecio) {
        productos.updateOne(
            Filters.eq("codigo", codigo),
            Updates.set("precio", nuevoPrecio)
        );
        System.out.println("Precio actualizado con éxito.");
    }

    public void eliminar(String codigo) {
        productos.deleteOne(Filters.eq("codigo", codigo));
        System.out.println("Producto eliminado correctamente.");
    }

    public void listarPocoInventario(int limite) {
        System.out.println("\n--- PRODUCTOS CON POCO INVENTARIO (< " + limite + ") ---");
        for (Document doc : productos.find(Filters.lt("existencia", limite))) {
            System.out.printf("Código: %s | Nombre: %s | Existencia: %d\n",
                    doc.getString("codigo"),
                    doc.getString("nombre"),
                    doc.getInteger("existencia"));
        }
    }

    @Override
    public void close() {
        cliente.close();
    }
}