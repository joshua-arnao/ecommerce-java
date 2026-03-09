import { configureStore } from '@reduxjs/toolkit';
import {
  useDispatch,
  useSelector,
  type TypedUseSelectorHook,
} from 'react-redux';
import { ShoppingCartSlice } from '../../features/shoppingCart/shoppingCartSlice';
import { accountSlice } from '../../features/account/accountSlice';

export const store = configureStore({
  reducer: {
    shoppingCart: ShoppingCartSlice.reducer,
    account: accountSlice.reducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
