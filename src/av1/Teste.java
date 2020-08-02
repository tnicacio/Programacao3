package av1;

import java.util.ArrayList;

public class Teste {
	
	public static void main(String[] args) {
	
		Ingredient ovo = new Ingredient("Ovo", "dúzia", 2);
		Ingredient leite = new Ingredient("Leite", "L", 1.5);
		Ingredient banana = new Ingredient("Banana", "un.", 4);
		
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(ovo);
		ingredients.add(leite);
		ingredients.add(banana);
		String howTo = "Faz desse jeito ó:\nColoca as coisas, mistura e tá pronto.";
		Recipe boloBanana = new Recipe("Bolo de Banana", "Aroldo Jr.", howTo, ingredients);
		
		ArrayList<Ingredient> ingredientesOvoFrito = new ArrayList<Ingredient>();
		ingredientesOvoFrito.add(ovo);
		Recipe ovoFrito = new Recipe("Ovo frito", "Pedo Bó", "Pega o ovo e frita",ingredientesOvoFrito);
		System.out.println(ingredients);
		System.out.println(boloBanana);
		System.out.println(ovoFrito);
	}
}
