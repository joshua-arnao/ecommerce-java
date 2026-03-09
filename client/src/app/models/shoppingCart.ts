export interface ShoppingCart {
  id: string;
  items: ShoppingCartItem[];
}

export interface ShoppingCartItem {
  id: number;
  name: string;
  description: string;
  price: number;
  pictureUrl: string;
  productBrand: string;
  productType: string;
  quantity: number;
}

export interface ShoppingCartTotals {
  shipping: number;
  subtotal: number;
  total: number;
}
