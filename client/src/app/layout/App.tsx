import {
  Container,
  createTheme,
  CssBaseline,
  ThemeProvider,
} from '@mui/material';
import Header from './Header';
import { useEffect, useState } from 'react';
import { Outlet } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import { getShoppingCartFromLocalStorage } from '../util/util';
import { useAppDispatch } from '../store/configureStors';
import { fetchCurrentUser } from '../../features/account/accountSlice';
import agent from '../api/agent';
import { setShoppingCart } from '../../features/shoppingCart/shoppingCartSlice';
import Spinner from './Spinner';

function App() {
  const [darkMode, setDarkMode] = useState(false);
  const paletteType = darkMode ? 'dark' : 'light';
  const dispatch = useAppDispatch();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const shoppingCart = getShoppingCartFromLocalStorage();
    dispatch(fetchCurrentUser());
    if (shoppingCart) {
      agent.ShoppingCart.get()
        .then((shoppingCart) => dispatch(setShoppingCart(shoppingCart)))
        .catch((error) => console.error(error))
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  });

  const theme = createTheme({
    palette: {
      mode: paletteType,
    },
  });
  function handleThemeChange() {
    setDarkMode(!darkMode);
  }
  if (loading) return <Spinner message='Gettin Basket...' />;
  return (
    <ThemeProvider theme={theme}>
      <ToastContainer position='bottom-right' hideProgressBar theme='colored' />
      <CssBaseline />
      <Header darkMode={darkMode} handleThemeChange={handleThemeChange} />
      <Container sx={{ paddingTop: '96px' }}>
        <Outlet />
      </Container>
    </ThemeProvider>
  );
}

export default App;
