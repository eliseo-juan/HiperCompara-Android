package com.hipercompara.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.commons.lang.StringEscapeUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hipercompara.logic.Brand;
import com.hipercompara.logic.Category;
import com.hipercompara.logic.Offer;
import com.hipercompara.logic.Product;
import com.hipercompara.logic.Recipe;
import com.hipercompara.logic.SharedPreferencesManager;
import com.hipercompara.logic.SubCategory;
import com.hipercompara.logic.Supermarket;
import com.hipercompara.logic.UnitType;

/**
 * Base de datos de la aplicacion,
 * sera complementaria a la base de datos del servidor web
 * con una finalidad de facilitar datos basicos para el uso
 * de la aplicacion sin necesidad de acceder al servidor web
 * @author Javier
 *
 */

public class HCdb extends SQLiteOpenHelper{
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public HCdb(Context context) {
		super(context, "HC.db", null, 1);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( "CREATE TABLE brand(" +
				"id INTEGER PRIMARY KEY," +
				"name TEXT UNIQUE NOT NULL);");

		db.execSQL(	"CREATE TABLE unitType(" +
				"id INTEGER PRIMARY KEY," +
				"name TEXT UNIQUE NOT NULL);");

		db.execSQL( "CREATE TABLE super(" +
				"id INTEGER PRIMARY KEY," +
				"name TEXT NOT NULL," +
				"country TEXT," +
				"city TEXT," +
				"postalCode INTEGER," +
				"adress TEXT);");

		db.execSQL( "CREATE TABLE listProduct(" +
				"id INTEGER PRIMARY KEY," +
				"times INTEGER NOT NULL," +
				"name TEXT NOT NULL," +
				"idBrand INTEGER," +
				"idCategory INTEGER," +
				"description TEXT," +
				"quantity FLOAT," +
				"idUnit INTEGER," +
				"avPrice INTEGER," +
				"CONSTRAINT pb FOREIGN KEY(idBrand) REFERENCES brand(id)," +
				"CONSTRAINT pb FOREIGN KEY(idCategory) REFERENCES category(id)," +
				"CONSTRAINT pu FOREIGN KEY(idUnit) REFERENCES unitType(id)" +");");

		db.execSQL("CREATE TABLE category(" +
				"id INTEGER PRIMARY KEY," +
				"name TEXT UNIQUE NOT NULL," +
				"iconName TEXT);");

		db.execSQL("CREATE TABLE subcategory(" +
				"id INTEGER PRIMARY KEY," +
				"categoryId INTEGER NOT NULL," +
				"name TEXT UNIQUE NOT NULL);");

		db.execSQL("CREATE TABLE offer(" +
				"productId INTEGER," +
				"superId INTEGER," +
				"percentage FLOAT," +
				"newPrice FLOAT," +
				"startDate TEXT," +
				"endDate TEXT," +
				"CONSTRAINT sp PRIMARY KEY(productId,superId)," +
				"CONSTRAINT pi FOREIGN KEY(productId) REFERENCES listProduct(id)," +
				"CONSTRAINT si FOREIGN KEY(superId) REFERENCES super(id))");

		db.execSQL( "CREATE TABLE recipe(" +
				"id INTEGER PRIMARY KEY," +
				"name TEXT NOT NULL," +
				"description TEXT)");

		db.execSQL( "CREATE TABLE recipeProduct(" +
				"idProduct INTEGER NOT NULL," +
				"idRecipe INTEGER NOT NULL," +
				"PRIMARY KEY(idProduct,idRecipe))");

		db.execSQL( "CREATE TABLE Product(" +
				"id INTEGER PRIMARY KEY," +
				"times INTEGER NOT NULL," +
				"name TEXT NOT NULL," +
				"idBrand INTEGER," +
				"idCategory INTEGER," +
				"description TEXT," +
				"quantity FLOAT," +
				"idUnit INTEGER," +
				"avPrice INTEGER," +
				"CONSTRAINT pb FOREIGN KEY(idBrand) REFERENCES brand(id)," +
				"CONSTRAINT pb FOREIGN KEY(idCategory) REFERENCES category(id)," +
				"CONSTRAINT pu FOREIGN KEY(idUnit) REFERENCES unitType(id)" +");");
		db.execSQL( "CREATE TABLE PurchasedProduct(" +
				"id INTEGER PRIMARY KEY," +
				"times INTEGER NOT NULL," +
				"name TEXT NOT NULL," +
				"idBrand INTEGER," +
				"idCategory INTEGER," +
				"description TEXT," +
				"quantity FLOAT," +
				"idUnit INTEGER," +
				"avPrice INTEGER," +
				"CONSTRAINT pb FOREIGN KEY(idBrand) REFERENCES brand(id)," +
				"CONSTRAINT pb FOREIGN KEY(idCategory) REFERENCES category(id)," +
				"CONSTRAINT pu FOREIGN KEY(idUnit) REFERENCES unitType(id)" +");");
		addCategories(db);
		addBrands(db);
		addUnitTypes(db);
		addRecipes(db);

		// Guarda en las preferencias el estado de la base de datos
		SharedPreferences preferences = SharedPreferencesManager.getDefaultPreferences();
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("dbCreated", true);
		editor.commit();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS brand");
		db.execSQL("DROP TABLE IF EXISTS unitType");
		db.execSQL("DROP TABLE IF EXISTS super");
		db.execSQL("DROP TABLE IF EXISTS product");
		db.execSQL("DROP TABLE IF EXISTS superProduct");
		db.execSQL("DROP TABLE IF EXISTS listProduct");
		db.execSQL("DROP TABLE IF EXISTS category");
		db.execSQL("DROP TABLE IF EXISTS subcategory");
		db.execSQL("DROP TABLE IF EXISTS offer");
		db.execSQL("DROP TABLE IF EXISTS recipe");
		db.execSQL("DROP TABLE IF EXISTS recipeProduct");
		db.execSQL("DROP TABLE IF EXISTS Product");
		onCreate(db);
	}

	/**
	 * Anade una marca a la base de datos, comprobando que no esta ya en ella
	 * @author Javier
	 *
	 */
	public void addBrand(String brand){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO brand VALUES (null,'"+brand+"')");
		db.close();
	}

	/**
	 * Obtiene una marca de la base de datos por su nombre, si no esta devuelve nulo
	 * @author Javier
	 *
	 */
	public Brand getBrandbyName(String name){
		SQLiteDatabase db = this.getReadableDatabase();
		Brand b=null;
		Cursor cursor = db.rawQuery("Select id,name from brand where name='"+name+"'", null);
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			b=new Brand();
			b.setId(cursor.getInt(0));
			b.setName(cursor.getString(1));
		}
		cursor.close();
		db.close();
		return b;
	}

	public Brand getBrandbyId(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Brand b=null;
		Cursor cursor = db.rawQuery("Select id,name from brand where id="+id+"", null);
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			b=new Brand();
			b.setId(cursor.getInt(0));
			b.setName(cursor.getString(1));
		}
		cursor.close();
		db.close();
		return b;
	}


	/**
	 * Anade una unidad de medida a la base de datos, comprobando que no esta ya en ella
	 * @author Javier
	 *
	 */
	public void addUnitType(UnitType ut){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO unitType VALUES ("+Integer.valueOf(ut.getId())+",'"+ut.getName()+"')");
		db.close();
	}

	/**
	 * Obtiene una marca de la base de datos por su nombre, si no esta devuelve nulo
	 * @author Javier
	 *
	 */
	public UnitType getUnitTypebyName(String name){
		SQLiteDatabase db = this.getReadableDatabase();
		UnitType b=null;
		Cursor cursor = db.rawQuery("Select id,name from unitType where name='"+name+"'", null);
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			b=new UnitType();
			b.setId(cursor.getInt(0));
			b.setName(cursor.getString(1));
		}
		cursor.close();
		db.close();
		return b;
	}

	public UnitType getUnitTypebyId(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		UnitType b=null;
		Cursor cursor = db.rawQuery("Select id,name from unitType where id="+id+"", null);
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			b=new UnitType();
			b.setId(cursor.getInt(0));
			b.setName(cursor.getString(1));
		}
		cursor.close();
		db.close();
		return b;
	}

	/**
	 * Devuelve un listado de todas las unittypes incluidas en la base de datos
	 * @author Javier
	 *
	 */
	public ArrayList<UnitType> getAllUnitTypes(){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<UnitType> list= new ArrayList<UnitType>();
		Cursor cursor = db.rawQuery("Select id,name from unitType", null);
		while (cursor.moveToNext()){
			UnitType u=new UnitType();
			u.setId(cursor.getInt(0));
			u.setName(cursor.getString(1)); 
			list.add(u);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * Anade un producto a la lista de la compra de la base de datos
	 * @author Javier  
	 * @param p
	 */
	public void addProductList(Product p){
		String query;
		query = "INSERT INTO listproduct VALUES (" +
				p.getId() + "," + 
				p.getTimesInList() + ",'" +
				p.getName() + "'," + 
				p.getBrandId() + "," +
				p.getCategoryId() + ",'" + 
				p.getDescription() + "'," + 
				p.getQuantity() + "," +
				p.getUnitTypeId() + "," +
				p.getAveragePrice() + ")";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(query);
		db.close();
	}

	/**
	 * Obtiene todos los productos de la lista de la compra de la base de datos
	 * Pueden haber dos productos iguales
	 * @author Javier  
	 *
	 */
	public ArrayList<Product> getAllProductsList(){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Product> list= new ArrayList<Product>();
		Cursor cursor = db.rawQuery("Select id,name,description,quantity,times,idCategory,idUnit,idBrand,avPrice from listProduct where id not in (select productId from offer)", null);
		while (cursor.moveToNext()){
			Product u=new Product();
			u.setId(cursor.getInt(0));
			u.setName(cursor.getString(1));
			u.setDescription(cursor.getString(2));
			u.setQuantity(cursor.getFloat(3));
			u.setTimesInList(cursor.getInt(4));
			u.setCategoryId(cursor.getInt(5));
			u.setUnitTypeId(cursor.getInt(6));
			u.setBrandId(cursor.getInt(7));
			u.setAveragePrice(cursor.getFloat(8));
			list.add(u);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * Anadimos un supermercado a la base de datos
	 * @return
	 */
	public void addSuper(Supermarket s){
		String query;
		query = "INSERT INTO super VALUES (" +
				s.getId() + ",'" + 
				s.getName() + "','" +
				s.getCountry() + "','" +
				s.getCity() + "'," +
				s.getPostalCode() + ",'" +
				s.getAddress() + "')";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(query);
		db.close();
	}

	public void updateTimesInList(int id, int times){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE listproduct SET times=" + times +  " WHERE id=" + id );
		db.close();
	}

	public void deleteAllList() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM listProduct");
		db.close();
	}

	/**
	 * Borramos todos los objetos de la tabla de ofertas
	 */
	public void deleteAllOffers() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM offer");
		db.close();
	}

	/**
	 * Borramos todos los supemercados
	 */
	public void deleteAllSupers() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM super");
		db.close();
	}

	public void deleteProductFromList(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM listProduct where id=" + Integer.toString(id));
		db.close();
	}

	/**
	 * 
	 * @param id
	 * @return true si hay algun producto con un identificador igual a id en la base de datos
	 * false si no hay ninguno
	 * 
	 */
	public boolean idExistsInList(int id){
		boolean answer = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * from listProduct where id="+id+"", null);
		if (cursor.getCount()>0) answer=true;
		cursor.close();
		db.close();
		return answer;
	}

	/**
	 * 
	 * @param id
	 * @return La cantidad de veces que hemos anadido a la lista un producto con esa id 
	 *
	 */
	public int getTimesInList(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select times from listProduct where id="+id+"", null);
		cursor.moveToFirst();
		int answer = cursor.getInt(0);
		cursor.close();
		db.close();
		return answer;
	}

	/**
	 * Mete las categorias y subcategorias en la base de datos local cuando se crea la base 
	 * de datos de la aplicacion.
	 * Las categorias al ser fijas se crean directamente, las subcategorias se obtienen del servidor
	 * @author Luis Miguel
	 */
	private void addCategories(SQLiteDatabase db) {
		// Crea las categorias
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(1, 'Bebidas', 'icono_bebida')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(2, 'Carnes y embutidos', 'icono_carne')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(3, 'Pescados', 'icono_pescado')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(4, 'Drogueria', 'icono_drogueria')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(5, 'Frutas y verduras', 'icono_fruta')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(6, 'Lacteos', 'icono_lacteos')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(7, 'Panaderia', 'icono_panaderia')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(8, 'Congelados', 'icono_congelados')");
		db.execSQL("INSERT INTO category(id, name, iconName) VALUES(9, 'Varios', 'icono_varios')");

		// Obtiene las subcategorias del servidor
		ParseJSONSubCategory pjsc = new ParseJSONSubCategory();
		ArrayList<SubCategory> subcategories = pjsc.getAllSubCategories();
		for(SubCategory sc: subcategories)
			db.execSQL("INSERT INTO subcategory(id, name, categoryId) VALUES (" + Integer.valueOf(sc.getId()) +  ",'" + StringEscapeUtils.unescapeHtml(sc.getName()) + "', '" + sc.getCategoryId() + "')");
	}

	/**
	 * Se baja las marcas del servidor y las guarda en la base de datos local
	 * @author Luis Miguel
	 */
	private void addBrands(SQLiteDatabase db) {
		ParseJSONBrand pjb = new ParseJSONBrand();
		ArrayList<Brand> brands = pjb.getAllBrands();
		for(Brand b: brands)
			db.execSQL("INSERT INTO brand(id, name) VALUES (" + Integer.valueOf(b.getId()) +  ",'" + StringEscapeUtils.unescapeHtml(b.getName()) + "')");

	}

	/**
	 * Se baja los tipos de unidad del servidor y los guarda en la base de datos local
	 * @author Luis Miguel
	 */
	private void addUnitTypes(SQLiteDatabase db) {
		ParseJSONUnitType pjut = new ParseJSONUnitType();
		ArrayList<UnitType> unitTypes = pjut.getAllUnitTypes();
		for(UnitType ut: unitTypes)
			db.execSQL("INSERT INTO unitType(id, name) VALUES (" + Integer.valueOf(ut.getId()) +  ",'" + StringEscapeUtils.unescapeHtml(ut.getName()) + "')");
	}

	/**
	 * Crea las recetas de la aplicacion, las recetas se crean aqui pero los 
	 * productos correspondientes se bajan de sde la web
	 * @author Luis Miguel
	 */
	private void addRecipes(SQLiteDatabase db) {
		db.execSQL("INSERT INTO recipe(id, name, description)" +
				" VALUES (1, 'Ensalada', 'Ensalada valenciana')");

		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (1,11)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (1,12)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (1,77)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (1,78)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (1,79)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (1,80)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (1,81)");


		db.execSQL("INSERT INTO recipe(id, name, description)" +
				" VALUES (2, 'Torrijas', 'Torrijas de leche')");

		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (2,18)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (2,21)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (2,78)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (2,82)");


		db.execSQL("INSERT INTO recipe(id, name, description)" +
				" VALUES (3, 'Desayuno', 'Desayuno energetico')");

		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (3,23)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (3,33)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (3,57)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (3,58)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (3,59)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (3,60)");
		db.execSQL("INSERT INTO recipeProduct(idRecipe, idProduct) VALUES (3,74)");

		addProductsFromRecipes(db);
	}

	/**
	 * Baja de la web y guarda en la base de datos local los productos 
	 * que se encuentran en las recetas creadas
	 * @author Luis Miguel
	 */
	private void addProductsFromRecipes(SQLiteDatabase db) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery("SELECT DISTINCT idProduct FROM recipeProduct", null);
		while (cursor.moveToNext())
			ids.add(cursor.getInt(0));

		for(Integer i: ids) {
			ParseJSONRecipeProduct pjpi = new ParseJSONRecipeProduct();
			Product p = pjpi.getProduct(i);

			String query;
			query = "INSERT INTO Product VALUES (" +
					p.getId() + "," + 
					p.getTimesInList() + ",'" +
					p.getName() + "'," + 
					p.getBrandId() + "," +
					p.getCategoryId() + ",'" + 
					p.getDescription() + "'," + 
					p.getQuantity() + "," +
					p.getUnitTypeId() + "," +
					p.getAveragePrice() + ")";
			db.execSQL(query);
		}
	}

	/**
	 * Obtiene una lista con todas las recetas
	 * @return lista de recetas
	 */
	public ArrayList<Recipe> getAllRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id,name,description FROM recipe", null);
		while (cursor.moveToNext()){
			Recipe r = new Recipe();
			int idRecipe = cursor.getInt(0);
			r.setId(idRecipe);
			r.setName(cursor.getString(1));
			r.setDescription(cursor.getString(2));
			r.setProducts(getProductsFromRecipe(idRecipe));
			recipes.add(r);
		}
		return recipes;
	}

	/**
	 * Obtiene los productos de una receta
	 * @param idRecipe identificador de la receta
	 * @return productos que contiene la receta
	 */
	public ArrayList<Product> getProductsFromRecipe(int idRecipe) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Product> list= new ArrayList<Product>();
		Cursor cursor = db.rawQuery("Select id,name,description,quantity,times,idCategory,idUnit,idBrand,avPrice" +
				" from Product " +
				"where id in (select idProduct from recipeProduct where idRecipe=" + idRecipe +")", null);
		while (cursor.moveToNext()){
			Product u=new Product();
			u.setId(cursor.getInt(0));
			u.setName(cursor.getString(1));
			u.setDescription(cursor.getString(2));
			u.setQuantity(cursor.getFloat(3));
			u.setTimesInList(cursor.getInt(4));
			u.setCategoryId(cursor.getInt(5));
			u.setUnitTypeId(cursor.getInt(6));
			u.setBrandId(cursor.getInt(7));
			u.setAveragePrice(cursor.getFloat(8));
			list.add(u);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * Obtiene todas las categorias existentes en la base de datos
	 * @return
	 */
	public ArrayList<Category> getAllCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id,name,iconName FROM category", null);
		while (cursor.moveToNext()){
			Category c = new Category();
			c.setId(cursor.getInt(0));
			c.setName(cursor.getString(1));
			c.setIconName(cursor.getString(2));
			categories.add(c);
		}
		cursor.close();
		db.close();
		return categories;
	}

	/**
	 * Este metodo devuelve una categoria pero sin sus subcategorias
	 * @param id identificador de la categoria
	 * @return
	 */
	public Category getCategoryById(int id) {
		ArrayList<Category> categories = new ArrayList<Category>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id,name,iconName FROM category WHERE id="+Integer.valueOf(id), null);
		while (cursor.moveToNext()){
			Category c = new Category();
			c.setId(cursor.getInt(0));
			c.setName(cursor.getString(1));
			c.setIconName(cursor.getString(2));
			categories.add(c);
		}
		cursor.close();
		db.close();
		if(categories.size() > 0)
			return categories.get(0);
		else
			return new Category(); 
	}

	/**
	 * Obtiene una lista de subcategorias a partir del id de la categoria
	 * @param categoryId identificador de la categoria
	 * @return
	 */
	public ArrayList<SubCategory> getSubCategories(int categoryId) {
		ArrayList<SubCategory> subcategories = new ArrayList<SubCategory>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id,name FROM subcategory WHERE categoryId=" + Integer.valueOf(categoryId), null);
		while (cursor.moveToNext()){
			SubCategory c = new SubCategory();
			c.setId(cursor.getInt(0));
			c.setName(cursor.getString(1));
			subcategories.add(c);
		}
		cursor.close();
		db.close();
		return subcategories;
	}

	/**
	 * Anade una oferta a la lista. Si existia el producto al que hacia referencia 
	 * le cambia el parametro times por 1 
	 * @param o
	 */
	public void addOffer(Offer o){
		Supermarket s= o.getSupermarket();
		Product p = o.getProduct();
		int idProd=p.getId();
		if(!superExistsInDatabase(s)) addSuper(s);
		if(!idExistsInList(idProd)) addProductList(p);
		try{
			String query;
			query = "INSERT INTO offer VALUES (" +
					idProd + "," +
					s.getId() + "," +
					o.getDiscountPercentage() + "," +
					o.getNewPrice() + ",'" +
					df.format(o.getStartDate()) + "','" +
					df.format(o.getEndDate()) + "')";
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(query);
			db.close();
		}
		catch(RuntimeException e){

		}
	}

	private boolean superExistsInDatabase(Supermarket s){
		boolean answer = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * from super where id="+s.getId()+"", null);
		if (cursor.getCount()>0) answer=true;
		cursor.close();
		db.close();
		return answer;
	}

	/**
	 * Obtenemos todas las ofertas almacenadas en la base de datos
	 * @return
	 */
	public ArrayList<Offer> getAllOffers(){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Offer> offers= new ArrayList<Offer>(); 
		Cursor cursor = db.rawQuery("Select productId,superId,percentage,newPrice,startDate,endDate from Offer", null);
		while (cursor.moveToNext()){
			Offer o=new Offer();
			o.setProduct(getProductById(cursor.getInt(0)));
			o.setSupermarket(getSuperById(cursor.getInt(1)));
			o.setDiscountPercentage(cursor.getFloat(2));
			o.setNewPrice(cursor.getFloat(3));
			try {
				o.setStartDate(df.parse(cursor.getString(4)));
				o.setEndDate(df.parse(cursor.getString(5)));
			} catch (ParseException e) {
			}
			offers.add(o);
		}
		cursor.close();
		db.close();
		return offers;
	}

	public Supermarket getSuperById(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Supermarket s=new Supermarket();
		Cursor cursor = db.rawQuery("Select id,name,country,city,postalCode,adress from super where id="+id+"", null);
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			s.setId(cursor.getInt(0));
			s.setName(cursor.getString(1));
			s.setCountry(cursor.getString(2));
			s.setCity(cursor.getString(3));
			s.setPostalCode(cursor.getInt(4));
			s.setAddress(cursor.getString(5));
		}
		cursor.close();
		db.close();
		return s;
	}

	public Product getProductById(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Product u= new Product();
		Cursor cursor = db.rawQuery("Select id,name,description,quantity,times,idCategory,idUnit,idBrand,avPrice from listProduct where id="+id, null);
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			u=new Product();
			u.setId(cursor.getInt(0));
			u.setName(cursor.getString(1));
			u.setDescription(cursor.getString(2));
			u.setQuantity(cursor.getFloat(3));
			u.setTimesInList(cursor.getInt(4));
			u.setCategoryId(cursor.getInt(5));
			u.setUnitTypeId(cursor.getInt(6));
			u.setBrandId(cursor.getInt(7));
			u.setAveragePrice(cursor.getInt(8));
		}
		cursor.close();
		db.close();
		return u;
	}	 

	/**
	 * Borra un objeto de la tabla de ofertas de la base de datos en base a los parametros
	 * que le pasan
	 * @param prod id del producto asociado a la oferta
	 * @param supermarket id del supermercado asociado a la oferta
	 * @param delProd TRUE si ademas de la oferta queremos eliminar en la tabla de productos
	 * el objeto asociado a la oferta
	 * @param delSuper TRUE si ademas de la oferta queremos eliminar en la tabla de supermercados
	 * el objeto asociado a la oferta
	 */
	public void deleteOfferbyIds(int prod,int supermarket,Boolean delProd,Boolean delSuper){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM offer where productId="+prod+" AND superId ="+supermarket);
		if(delProd) db.execSQL("DELETE FROM listProduct where id="+prod);
		if(delSuper) db.execSQL("DELETE FROM super where id="+supermarket);
		db.close();
	}

	/**
	 * Funcion que obtiene de la base de datos los n productos mas comprados
	 * @return list 
	 */
	public ArrayList<Product> getMorePurchasedProducts(int n){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Product> list= new ArrayList<Product>();
		Cursor cursor = db.rawQuery("Select id,name,description,quantity,times,idCategory,idUnit,idBrand,avPrice from purchasedProduct order by times DESC LIMIT "+n, null);
		while (cursor.moveToNext()){
			Product u=new Product();
			u.setId(cursor.getInt(0));
			u.setName(cursor.getString(1));
			u.setDescription(cursor.getString(2));
			u.setQuantity(cursor.getFloat(3));
			u.setTimesInList(1);
			u.setCategoryId(cursor.getInt(5));
			u.setUnitTypeId(cursor.getInt(6));
			u.setBrandId(cursor.getInt(7));
			u.setAveragePrice(cursor.getFloat(8));
			list.add(u);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * Funcion que anade los productos que estan en la lista de la compra
	 * en el historial de los productos mas comprados
	 */
	public void addPurchasedProducts(){
		ArrayList<Product> products= getAllProductsList();
		for(Product p:products){	
			String query;
			query = "INSERT OR REPLACE INTO purchasedProduct VALUES (" +
					p.getId() + "," + 
					(p.getTimesInList() + getTimesPurchased(p.getId())) + ",'" +
					p.getName() + "'," + 
					p.getBrandId() + "," +
					p.getCategoryId() + ",'" + 
					p.getDescription() + "'," + 
					p.getQuantity() + "," +
					p.getUnitTypeId() + "," +
					p.getAveragePrice() + ")";
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(query);
			db.close();
		}		
	}
	/**
	 * 
	 * @param id
	 * @return Devuelve las veces que ha sido comprado el producto asociado a ese id
	 */
	public int getTimesPurchased(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select times from purchasedProduct where id="+id+"", null);
		int answer=0;
		while(cursor.moveToNext()) answer=cursor.getInt(0);
		cursor.close();
		db.close();
		return answer;
	}


}
