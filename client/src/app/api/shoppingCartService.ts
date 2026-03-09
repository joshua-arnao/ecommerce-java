import axios from 'axios';
import type {
  ShoppingCart,
  ShoppingCartItem,
  ShoppingCartTotals,
} from '../models/shoppingCart';
import type { Dispatch } from 'redux';
import type { Product } from '../models/product';
import { setShoppingCart } from '../../features/shoppingCart/shoppingCartSlice';
import { createId } from '@paralleldrive/cuid2';

class ShoppingCartService {
  apiUrl = 'http://localhost:8081/api/shopping-carts';

  async getShoppingFromCartApi() {
    try {
      const response = await axios.get<ShoppingCart>(`${this.apiUrl}`);
      return response.data;
    } catch (error) {
      throw new Error('Failes to retrive the shoppingCart' + error);
    }
  }

  async getShoppingCart() {
    try {
      const shoppingCart = localStorage.getItem('shopping_cart');
      if (shoppingCart) {
        return JSON.parse(shoppingCart) as ShoppingCart;
      } else {
        throw new Error('ShoppingCart not found in local storage');
      }
    } catch (error) {
      throw new Error('Failed to retrive the shoppingCart: ' + error);
    }
  }

  async addItemToShoppingCart(item: Product, quantity = 1, dispatch: Dispatch) {
    try {
      let shoppingCart = this.getCurrentShoppingCart();
      if (!shoppingCart) {
        shoppingCart = await this.createShoppingCart();
      }
      const itemToAdd = this.mapProductToShoppingCart(item);
      shoppingCart.items = this.upsertItems(
        shoppingCart.items,
        itemToAdd,
        quantity,
      );
      this.setShoppingCart(shoppingCart, dispatch);
      //calculate totals
      const totals = this.calculateTotals(shoppingCart);
      return { shoppingCart, totals };
    } catch (error) {
      throw new Error('Failed to add and intem to ShoppingCart.' + error);
    }
  }

  async remove(itemId: number, dispatch: Dispatch) {
    const shoppingCart = this.getCurrentShoppingCart();
    if (shoppingCart) {
      const itemIndex = shoppingCart.items.findIndex((p) => p.id === itemId);
      if (itemIndex !== -1) {
        shoppingCart.items.splice(itemIndex, 1);
        this.setShoppingCart(shoppingCart, dispatch);
      }
      //check if shoppingCart is empty after removing the item
      if (shoppingCart.items.length === 0) {
        //clear the the shoppingCart from the local storage
        localStorage.removeItem('shopping_cart_id');
        localStorage.removeItem('shopping_cart');
      }
    }
  }

  async incrementItemQuantity(
    itemId: number,
    quantity: number = 1,
    dispatch: Dispatch,
  ) {
    const shoppingCart = this.getCurrentShoppingCart();
    if (shoppingCart) {
      const item = shoppingCart.items.find((p) => p.id === itemId);
      if (item) {
        item.quantity += quantity;
        if (item.quantity < 1) {
          item.quantity = 1;
        }
        this.setShoppingCart(shoppingCart, dispatch);
      }
    }
  }

  async decrementItemQuantity(
    itemId: number,
    quantity: number = 1,
    dispatch: Dispatch,
  ) {
    const shoppingCart = this.getCurrentShoppingCart();
    if (shoppingCart) {
      const item = shoppingCart.items.find((p) => p.id === itemId);
      if (item && item.quantity > 1) {
        item.quantity -= quantity;
        this.setShoppingCart(shoppingCart, dispatch);
      }
    }
  }

  async deleteshoppingCart(shoppingCartId: string): Promise<void> {
    try {
      await axios.delete(`${this.apiUrl}/${shoppingCartId}`);
    } catch (error) {
      throw new Error('Failed to delete the shoppingCart.');
    }
  }

  async setShoppingCart(shoppingCart: ShoppingCart, dispatch: Dispatch) {
    try {
      await axios.post<ShoppingCart>(this.apiUrl, shoppingCart);
      localStorage.setItem('shopping_cart', JSON.stringify(shoppingCart));
      dispatch(setShoppingCart(shoppingCart));
    } catch (error) {
      throw new Error('Failed to update Shopping Cart.' + error);
    }
  }

  private getCurrentShoppingCart() {
    const shoppingCart = localStorage.getItem('shopping_cart');
    return shoppingCart ? (JSON.parse(shoppingCart) as ShoppingCart) : null;
  }

  private async createShoppingCart(): Promise<ShoppingCart> {
    try {
      const newShoppingCartt: ShoppingCart = {
        id: createId(),
        items: [],
      };
      localStorage.setItem('shopping_cart_id', newShoppingCartt.id);
      return newShoppingCartt;
    } catch (error) {
      throw new Error('Failed to create ShoppingCart.');
    }
  }
  private mapProductToShoppingCart(item: Product): ShoppingCartItem {
    return {
      id: item.id,
      name: item.name,
      price: item.price,
      description: item.description,
      quantity: 0,
      pictureUrl: item.pictureUrl,
      productBrand: item.productBrand,
      productType: item.productType,
    };
  }
  private upsertItems(
    items: ShoppingCartItem[],
    itemTotAdd: ShoppingCartItem,
    quantity: number,
  ): ShoppingCartItem[] {
    const existingItem = items.find((x) => x.id == itemTotAdd.id);
    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      itemTotAdd.quantity = quantity;
      items.push(itemTotAdd);
    }
    return items;
  }
  private calculateTotals(shoppingCart: ShoppingCart): ShoppingCartTotals {
    const shipping = 0;
    const subtotal = shoppingCart.items.reduce(
      (acc, item) => acc + item.price * item.quantity,
      0,
    );
    const total = shipping + subtotal;
    return { shipping, subtotal, total };
  }
}

export default new ShoppingCartService();
