package com.hipercompara.logic;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import com.hipercompara.dao.HCdbManager;
import com.hipercompara.dao.ParseJSONProductByID;
import com.hipercompara.dao.ParseJSONProductByName;

/**
 * ProductSearchEngine sustituye a las clases CategoryManager y ProductManager
 * que solo servian para buscar productos
 * Tambien se puede usar para buscar los productos recientemente comprados
 * @author Luis Miguel
 *
 */
public class ProductSearchEngine {
	
	/**
	 * Constructor
	 */
	private ProductSearchEngine() {
		super();
		oldPrefix = "";
		products = new ArrayList<Product>();
	}
	
	/**
	 * Devuelve la unica instancia de la clase
	 * @return instancia de la clase
	 */
	public static ProductSearchEngine instance() {
		if(engine == null)
			engine = new ProductSearchEngine();
		return engine;
	}
	
	/**
	 * Devuelve una lista de productos a partir 
	 * de un prefijo del nombre (se puede usar mientras vamos tecleando el nombre)
	 * @return lista con los productos
	 */
	public ArrayList<Product> search(String prefix) {
		String searchPrefix = prefix.toLowerCase();
		
		/* Optimizacion, ejemplo explicativo: 
		 * Si hemos buscado "le" y en la siguiente busqueda se busca "lec"
		 * no hace falta bajarse los productos de nuevo del servidor 
		 * ya que el conjunto de productos que empiezan por "lec" esta
		 * incluido en el de los que empiezan por "le".
		 */
		if( oldPrefix.equals("") || !searchPrefix.startsWith(oldPrefix) ) {
			/* Si el nuevo prefijo no comienza por el ultimo prefijo
			 * hace una consulta al servidor
			 * */
			try {
				System.out.println("JSON");
				ParseJSONProductByName pb = new ParseJSONProductByName();
				pb.execute(prefix);
				pb.get();
				products = pb.getAllProducts();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	
		oldPrefix = searchPrefix;
		return getOrderedProducts(products, searchPrefix);
	}
	
	/**
	 * Obtiene un producto a partir de su identificador
	 * @param id identificador del producto
	 * @return producto con ese identificador
	 */
	public Product getProduct(int id) {
		Product product = null;
		ParseJSONProductByID pi = new ParseJSONProductByID();
		pi.execute(id);
		try {
			pi.get();
			product = pi.getProduct();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return product;
	}

	/**
	 * Obtiene todas las categorias para poderlas mostrar por pantalla,
	 * esto solo se hace la primera vez que se llama al metodo, el resto
	 * de veces ya estan cargadas
	 * @return Lista con las categorias existentes
	 */
	public ArrayList<Category> getCategories() {
		if(categories == null) {
			categories = HCdbManager.getDb().getAllCategories();
		}
		return categories;
	}
	
	/**
	 * Obtiene la categoria con el nombre dado
	 * @return La categoria correspondiente al nombre o null si no existe
	 */
	public Category getCategoryByName(String name) {
		if(categories == null)
			getCategories();
		for(Category c: categories) {
			if(c.getName() == name)
				return c;
		}
		return null;
	}
	
	/**
	 * Obtiene la categoria con el id dado
	 * @return La categoria correspondiente al id o null si no existe
	 */
	public Category getCategoryById(int id) {
		if(categories == null)
			getCategories();
		for(Category c: categories) {
			if(c.getId() == id)
				return c;
		}
		return null;
	}
	
	/**
	 * Obtiene la lista de los productos mas comprados
	 * Esto se usara en la busqueda por categorias en un apartado 
	 * especial para que podamos añadir rapidamente los productos
	 * que mas hemos comprado
	 * @return lista de productos mas comprados
	 */
	public ArrayList<Product> getMostPurchasedProducts() {
		return HCdbManager.getDb().getMorePurchasedProducts(AppConstants.defaultNumberOfProductsInHistoric);
	}
	
	/**
	 * Obtiene la lista de productos ordenada de forma que primero aparecen los que 
	 * empiezan por el prefijo y despues los que contienen el prefijo en algun
	 * lugar pero no empiezan por el
	 * @return lista de productos
	 */
	private ArrayList<Product> getOrderedProducts(ArrayList<Product> products, String prefix) {
		ArrayList<Product> orderedProducts = new ArrayList<Product>();
		
		for(Product p: products) {
			String productName = p.getName().toLowerCase();
			if(productName.startsWith(prefix))
				orderedProducts.add(p);
		}
		
		for(Product p: products) {
			String productName = p.getName().toLowerCase();
			if(!productName.startsWith(prefix) && productName.contains(prefix))
				orderedProducts.add(p);
		}
		
		return orderedProducts;
	}
	
	private ArrayList<Product> products;    // Productos obtenidos de la web
	private ArrayList<Category> categories; // Categorias
	private String oldPrefix;               // Prefijo de la busqueda por nombre (optimizacion)
	private static ProductSearchEngine engine;     // Unica instancia de la clase
}
