package av3.model;

import java.util.ArrayList;

public class Recipe{

	
	private String title;
	private String author;
	private String howTo;
	private ArrayList<Ingredient> ingredients = new ArrayList<>();

	
	public Recipe(String title, String author, String howTo,ArrayList<Ingredient> ingredients) {
		try {
			boolean existenceCondition = 
						title != null && !title.trim().equals("") &&
						author != null && !author.trim().equals("") &&
						howTo != null && !howTo.trim().equals("")
						&& !ingredients.isEmpty();
			
			if (!existenceCondition) {
				throw new Error("Title, Author, HowTo and Ingredients are required fields!");
			}

			setTitle(title);
			setAuthor(author);
			setHowTo(howTo);
			setIngredients(ingredients);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	
	@Override
	public String toString() {
		String comma = ", ";
		String newLine = "\n";
		StringBuilder sb = new StringBuilder()
				.append("{")
				.append("title: ").append(getTitle()).append(comma).append(newLine)
				.append("author: ").append(getAuthor()).append(comma).append(newLine)
				.append("howTo: ").append(getHowTo()).append(comma).append(newLine)
				.append("ingredients: ").append("[");

		if (!ingredients.isEmpty()) {
			int i;
			for (i = 0; i < ingredients.size() - 1; i++) {
				sb.append(ingredients.get(i)).append(comma);
			}
			sb.append(ingredients.get(i));
		}
		sb.append("]").append(comma).append(newLine)
		.append("}");
		return sb.toString();
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return this.author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getHowTo() {
		return howTo;
	}
	public void setHowTo(String howTo) {
		this.howTo = howTo;
	}
	
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
}
