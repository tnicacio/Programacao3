package av2.dao;

import java.util.ArrayList;

import av3.model.Recipe;

public interface InterfaceRecipeDAO {
	
	boolean inserir(Recipe recipe);
	boolean remover(String title);
	Recipe updateRecipe(String title, Object[] data);
	Recipe findByName(String title);
	ArrayList<Recipe> listRecipes();

}
