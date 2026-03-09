import type { ShoppingCart } from '../models/shoppingCart';

export function getShoppingCartFromLocalStorage(): ShoppingCart | null {
  const storedShoppingCart = localStorage.getItem('shopping-cart');
  if (storedShoppingCart) {
    try {
      const parsedShoppingCart: ShoppingCart = JSON.parse(storedShoppingCart);
      return parsedShoppingCart;
    } catch (error) {
      console.error('Error Parsing Shopping Caart from local storage: ', error);
      return null;
    }
  }
  return null;
}
