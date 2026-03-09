import {
  Avatar,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  CardMedia,
  CircularProgress,
  Typography,
} from '@mui/material';
import { LoadingButton } from '@mui/lab';
import type { Product } from '../../app/models/product';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { useAppDispatch } from '../../app/store/configureStors';
import agent from '../../app/api/agent';
import { setShoppingCart } from '../shoppingCart/shoppingCartSlice';

interface Props {
  product: Product;
}

export default function ProductCard({ product }: Props) {
  const { id, name, description, price, productBrand, productType } = product;

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

  const [loading, setLoading] = useState(false);
  const dispatch = useAppDispatch();

  function addItem() {
    setLoading(true);
    agent.ShoppingCart.addItem(product, dispatch)
      .then((response) => {
        console.log('New Basket:', response.shoppingCart);
        dispatch(setShoppingCart(response.shoppingCart));
      })
      .catch((error) => console.log(error))
      .finally(() => setLoading(false));
  }

  return (
    <Card>
      <CardHeader
        avatar={
          <Avatar sx={{ bgcolor: 'secondary.main' }}>
            {name.charAt(0).toUpperCase()}
          </Avatar>
        }
        title={name}
        titleTypographyProps={{
          sx: { fontWeight: 'bold', color: 'primary.main' },
        }}
      />

      <CardMedia
        sx={{ height: 140, backgroundSize: 'contain' }}
        image={'/images/products/' + extractImageName(product)}
        title={name}
      />

      <CardContent>
        <Typography gutterBottom color='secondary' variant='h5'>
          {formatPrice(price)}
        </Typography>
        <Typography variant='body2' color='text.secondary'>
          {productBrand} / {productType}
        </Typography>
      </CardContent>

      <CardActions>
        <LoadingButton
          loading={loading}
          onClick={addItem}
          size='small'
          startIcon={
            loading ? <CircularProgress size={20} color='inherit' /> : null
          }
        >
          Add to card
        </LoadingButton>
        <Button component={Link} to={`/store/${product.id}`} size='small'>
          View
        </Button>
      </CardActions>
    </Card>
  );
}
