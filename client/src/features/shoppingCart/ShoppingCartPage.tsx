import { Add, Delete, Remove } from '@mui/icons-material';
import {
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import { useAppDispatch, useAppSelector } from '../../app/store/configureStors';
import agent from '../../app/api/agent';
import type { Product } from '../../app/models/product';

export default function ShoppingCartPage() {
  const { shoppingCart } = useAppSelector((state) => state.shoppingCart);
  const dispatch = useAppDispatch();
  const { ShoppingCart: ShoppingCartActions } = agent;

  const removeItem = (productId: number) => {
    ShoppingCartActions.removeItem(productId, dispatch);
  };

  const decrementItem = (productId: number, quantity: number = 1) => {
    ShoppingCartActions.decrementItemQuantity(productId, quantity, dispatch);
  };

  const incrementItem = (productId: number, quantity: number = 1) => {
    ShoppingCartActions.incrementItemQuantity(productId, quantity, dispatch);
  };

  const extractImageName = (item: Product): string | null => {
    if (item && item.pictureUrl) {
      const parts = item.pictureUrl.split('/');
      if (parts.length > 0) {
        return parts[parts.length - 1];
      }
    }
    return null;
  };

  const formatPrice = (price: number): string => {
    return new Intl.NumberFormat('en-PE', {
      style: 'currency',
      currency: 'PEN',
      minimumFractionDigits: 2,
    }).format(price);
  };

  if (!shoppingCart || shoppingCart.items.length === 0)
    return (
      <Typography variant='h3'>
        Your basket is empty. Please add few items
      </Typography>
    );

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Product Image</TableCell>
            <TableCell>Product</TableCell>
            <TableCell>Price</TableCell>
            <TableCell>Quantity</TableCell>
            <TableCell>Subtotal</TableCell>
            <TableCell>Remove</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {shoppingCart.items.map((item) => (
            <TableRow key={item.id}>
              <TableCell>
                {item.pictureUrl && (
                  <img
                    src={'/images/products/' + extractImageName(item)}
                    alt='Product'
                    width='50'
                    height='50'
                  />
                )}
              </TableCell>
              <TableCell>{item.name}</TableCell>
              <TableCell>{formatPrice(item.price)}</TableCell>
              <TableCell>
                <IconButton
                  color='error'
                  onClick={() => decrementItem(item.id)}
                >
                  <Remove />
                </IconButton>
                {item.quantity}
                <IconButton
                  color='error'
                  onClick={() => incrementItem(item.id)}
                >
                  <Add />
                </IconButton>
              </TableCell>
              <TableCell>{formatPrice(item.price * item.quantity)}</TableCell>
              <TableCell>
                <IconButton
                  onClick={() => removeItem(item.id)}
                  aria-label='delete'
                >
                  <Delete />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
