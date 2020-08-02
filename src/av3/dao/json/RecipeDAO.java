package av3.dao.json;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;

import av3.dao.InterfaceRecipeDAO;
import av3.model.Ingredient;
import av3.model.Recipe;

public class RecipeDAO implements InterfaceRecipeDAO {

	private String path = "C:/Users/tnica/eclipse-workspace/prog3-avs/src/av3/";
	private String fileName = "recipes.json";

	@Override
	public boolean inserir(Recipe recipe) {

		// Creates an ingredient json-array with the recipe ingredients list
		JsonArrayBuilder ingredientsArray = Json.createArrayBuilder();
		if (recipe != null) {
			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredientsArray.add(Json.createObjectBuilder()
						.add("name", ingredient.getName()).add("unit", ingredient.getUnit())
						.add("quantity", ingredient.getQuantity()));
			}
			;
		}
		JsonArray ingredientsJson = ingredientsArray.build();

		// Creates a json object with the recipe data
		JsonObject newJsonObject = Json.createObjectBuilder()
				.add("title", recipe.getTitle())
				.add("author", recipe.getAuthor())				
				.add("howTo", recipe.getHowTo())
				.add("ingredients", ingredientsJson)
				.build();

		String record = "";
		File f = new File(path + fileName);

		if (f.exists() && !f.isDirectory()) {
			try {
				String content = new String(Files.readAllBytes(Paths.get(path + fileName)));
				JsonReader reader = Json.createReader(new StringReader(content));
				JsonArray ja = reader.readArray();
				reader.close();
				JsonArrayBuilder jab = Json.createArrayBuilder();
				for (int i = 0; i < ja.size(); i++) {
					JsonObject tmp = (JsonObject) ja.get(i);
					jab.add(tmp);
				}
				jab.add(newJsonObject);
				record = jab.build().toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JsonArray jab = Json.createArrayBuilder().add(newJsonObject).build();
			record = jab.toString();
		}
		try {
			Files.write(Paths.get(path + fileName), record.getBytes("utf-8"), StandardOpenOption.CREATE);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean remover(String title) {
		String gravar = "";
		File f = new File(path + fileName);
		int found = -1;
		
		if (f.exists() && !f.isDirectory()) {
			try {
				String content = new String(Files.readAllBytes(Paths.get(path + fileName)));
				JsonReader reader = Json.createReader(new StringReader(content));
				JsonArray ja = reader.readArray();
				reader.close();
				JsonArrayBuilder jab = Json.createArrayBuilder();
			
				for (int i = 0; i < ja.size(); i++) {
					
					JsonObject tmp = (JsonObject) ja.get(i);
					
					//Does not add the found object to the database file to be rewritten
					if (!tmp.getString("title").trim().equals(title.trim())) {
						jab.add(tmp);
					} else {
						found = i;
					}
				}
				
				if (found > -1) {
					gravar = jab.build().toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if (found > -1) {
					resetData();
					Files.write(Paths.get(path + fileName), gravar.getBytes("utf-8"), StandardOpenOption.CREATE);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return found != -1;
	}

	@Override
	public Recipe updateRecipe(String title, Object[] data) {
		Recipe recipe = findByName(title);
		//If a recipe was found then
		if (recipe != null) {
			
			String newTitle = (String) data[0];
			String newAuthor = (String) data[1];
			String newHowTo= (String) data[2];
			@SuppressWarnings("unchecked")
			ArrayList<Ingredient> newIngredientes = (ArrayList<Ingredient>) data[3];
			
			Recipe updatedRecipe = new Recipe(newTitle, newAuthor, newHowTo, newIngredientes);
			
			remover(title);
			inserir(updatedRecipe);
			return updatedRecipe;
		}
		return null;
	}

	@Override
	public Recipe findByName(String title) {
		Recipe recipe = null;
		JsonObject tmp = null;
		int found = -1;
		File f = new File(path + fileName);
		
		
		if (f.exists() && !f.isDirectory()) {
			try {
				
				String content = new String(Files.readAllBytes(Paths.get(path + fileName)));
				JsonReader reader = Json.createReader(new StringReader(content));
				JsonArray ja = reader.readArray();
				reader.close();
			
				for (int i = 0; i < ja.size(); i++) {
	
					tmp = (JsonObject) ja.get(i);
					if (tmp.getString("title").trim().equals(title.trim())) {
						found = i;
						break;
					}
				}
				
				if (found > -1) {
					
					ArrayList<Ingredient> listaIngredientes = getIngredientList(tmp);
					recipe = new Recipe(tmp.getString("title").toString(),
										tmp.getString("author").toString(),
										tmp.getString("howTo").toString(),
										listaIngredientes);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		return recipe;
	}

	@Override
	public ArrayList<Recipe> listRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<>();

		File f = new File(path + fileName);

		if (f.exists() && !f.isDirectory()) {
			try {
				
				//Reads json "database" file
				String content = new String(Files.readAllBytes(Paths.get(path + fileName)));
				JsonReader reader = Json.createReader(new StringReader(content));
				JsonArray ja = reader.readArray();

				//turn each json object into a Recipe
				for (int i = 0; i < ja.size(); i++) {

					JsonObject tmp = (JsonObject) ja.get(i);
					ArrayList<Ingredient> listaIngredientes = getIngredientList(tmp);

					Recipe recipe = new Recipe(tmp.getString("title"), tmp.getString("author"), tmp.getString("howTo"),
							listaIngredientes);
					//Adds the recipe to the recipes list 
					recipes.add(recipe);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return recipes;
	}

	private ArrayList<Ingredient> getIngredientList(JsonObject tmp) {
		ArrayList<Ingredient> ingredients = new ArrayList<>();

		try {
			//Gets the ingredient list as json-array
			JsonArray listaIngArray = tmp.get("ingredients").asJsonArray();

			for (int j = 0; j < listaIngArray.size(); j++) {
			
				//Treats the json data and creates an Ingredient with it
				JsonObject ingredienteObject = listaIngArray.get(j).asJsonObject();
				String name = ingredienteObject.get("name").toString().replace("\"", "");
				String unit = ingredienteObject.get("unit").toString().replace("\"", "");
				Double quantity = Double.parseDouble(ingredienteObject.get("quantity").toString().replace("\"", ""));

				Ingredient novo = new Ingredient(name, unit, quantity);
				//Adds the ingredient to the ingredients list
				ingredients.add(novo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ingredients;
	}
	
	public void resetData() {
		Path p = Paths.get(path + fileName);
		if (Files.exists(p)) {
			try {
				Files.delete(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
