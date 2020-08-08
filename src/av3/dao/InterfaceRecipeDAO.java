package av3.dao;

import java.util.ArrayList;

import av3.model.Recipe;

public interface InterfaceRecipeDAO {
	
	void insert(Recipe recipe) throws DAOException;
	void remove(String title) throws DAOException;
	Recipe updateRecipe(String title, Object[] data) throws DAOException;
	Recipe findByName(String title);
	ArrayList<Recipe> listRecipes();

}
