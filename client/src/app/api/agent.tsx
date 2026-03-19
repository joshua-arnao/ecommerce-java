import axios, { AxiosError, type AxiosResponse } from 'axios';
import { router } from '../router/Routes';
import { toast } from 'react-toastify';
import shoppingCartService from './shoppingCartService';
import type { Product } from '../models/product';
import type { Dispatch } from 'redux';
import type { ShoppingCart } from '../models/shoppingCart';

axios.defaults.baseURL = 'http://localhost:8081/api/';

const idle = () => new Promise((resolve) => setTimeout(resolve, 100));

const responseBody = (response: AxiosResponse) => response.data;

axios.interceptors.response.use(
  async (response) => {
    await idle();
    return response;
  },
  (error: AxiosError) => {
    const { status } = error.response as AxiosResponse;
    switch (status) {
      case 404:
        toast.error('Resource not found');
        router.navigate('/not-found');
        break;
      case 500:
        toast.error('Internal server error occurred');
        router.navigate('/server-error');
        break;
      default:
        break;
    }
    return Promise.reject(error.message);
  },
);

const requests = {
  get: (url: string) => axios.get(url).then(responseBody),
  post: (url: string, body: object) => axios.post(url, body).then(responseBody),
  put: (url: string, body: object) => axios.put(url, body).then(responseBody),
  delete: (url: string) => axios.delete(url).then(responseBody),
};

const Store = {
  apiUrl: 'http://localhost:8081/api/products',
  list: (
    page: number,
    size: number,
    brandId?: number,
    typeId?: number,
    url?: string,
  ) => {
    let requestsUrl = url || `products?page=${page - 1}&size=${size}`;
    if (brandId !== undefined) {
      requestsUrl += `&brandId=${brandId}`;
    }
    if (typeId !== undefined) {
      requestsUrl += `&typeId=${typeId}`;
    }
    return requests.get(requestsUrl);
  },
  details: (id: number) => requests.get(`products/${id}`),
  types: () =>
    requests
      .get('products/types')
      .then((types) => [{ id: 0, name: 'All' }, ...types]),
  brands: () =>
    requests
      .get('products/brands')
      .then((brands) => [{ id: 0, name: 'All' }, ...brands]),
  search: (keyword: string) => requests.get(`products?keywords=${keyword}`),
};

const ShoppingCart = {
  get: async () => {
    try {
      return await shoppingCartService.getShoppingCart();
    } catch (error) {
      console.error('Failed to get Basket: ', error);
      throw error;
    }
  },
  addItem: async (product: Product, dispatch: Dispatch) => {
    try {
      const result = await shoppingCartService.addItemToShoppingCart(
        product,
        1,
        dispatch,
      );
      return result;
    } catch (error) {
      console.error('Failed to add new Item to shoppingcart', error);
      throw error;
    }
  },
  removeItem: async (itemId: number, dispatch: Dispatch) => {
    try {
      await shoppingCartService.remove(itemId, dispatch);
    } catch (error) {
      console.error('Failed to remove an item from shoppingcart');
      throw error;
    }
  },
  incrementItemQuantity: async (
    itemId: number,
    quantity: number = 1,
    dispatch: Dispatch,
  ) => {
    try {
      await shoppingCartService.incrementItemQuantity(
        itemId,
        quantity,
        dispatch,
      );
    } catch (error) {
      console.error('Failed to increment item quantity in basket:', error);
      throw error;
    }
  },
  decrementItemQuantity: async (
    itemId: number,
    quantity: number = 1,
    dispatch: Dispatch,
  ) => {
    try {
      await shoppingCartService.decrementItemQuantity(
        itemId,
        quantity,
        dispatch,
      );
    } catch (error) {
      console.error('Failed to decrement item quantity in basket:', error);
      throw error;
    }
  },
  setBasket: async (basket: ShoppingCart, dispatch: Dispatch) => {
    try {
      await shoppingCartService.setShoppingCart(basket, dispatch);
    } catch (error) {
      console.error('Failed to set basket:', error);
      throw error;
    }
  },
  deleteshoppingCart: async (basketId: string) => {
    try {
      await shoppingCartService.deleteshoppingCart(basketId);
    } catch (error) {
      console.log('Failed to delete the Basket');
      throw error;
    }
  },
};

const Account = {
  login: (values: any) => requests.post('auth/login', values),
};

const Orders = {
  list: () => requests.get('orders'),
  fetch: (id: number) => requests.get(`orders/${id}`),
  create: (values: any) => requests.post('orders', values),
};

const agent = {
  Store,
  ShoppingCart,
  Account,
  Orders,
};

export default agent;
