package av2.dao;

import java.util.ArrayList;

import av2.model.Recipe;

public interface InterfaceRecipeDAO {
	
	boolean insert(Recipe recipe);
	boolean remove(String title);
	Recipe updateRecipe(String title, Object[] data);
	Recipe findByName(String title);
	ArrayList<Recipe> listRecipes();

}
