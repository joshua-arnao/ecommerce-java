import { createSlice } from '@reduxjs/toolkit';
import type { ShoppingCart } from '../../app/models/shoppingCart';

interface ShoppingCartState {
  shoppingCart: ShoppingCart | null;
}

const initialState: ShoppingCartState = {
  shoppingCart: null,
};

export const ShoppingCartSlice = createSlice({
  name: 'shoppingCart',
  initialState,
  reducers: {
    setShoppingCart: (state, action) => {
      console.log('new shopping cart state', action.payload);
      state.shoppingCart = action.payload;
    },
  },
});

export const { setShoppingCart } = ShoppingCartSlice.actions;
