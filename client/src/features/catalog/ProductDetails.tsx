import {
  Divider,
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import type { Product } from '../../app/models/product';
import agent from '../../app/api/agent';
import Spinner from '../../app/layout/Spinner';

export default function ProductDetails() {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    id &&
      agent.Store.details(parseInt(id))
        .then((response) => setProduct(response))
        .catch((error) => console.error(error))
        .finally(() => setLoading(false));
  }, [id]);

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

  if (loading) return <Spinner message='Loading Products...' />;

  return (
    product && (
      <Grid container spacing={6}>
        <Grid size={{ xs: 6 }}>
          <img
            src={'/images/products/' + extractImageName(product)}
            alt={product.name}
            style={{ width: '100%' }}
          />
        </Grid>
        <Grid size={{ xs: 6 }}>
          <Typography variant='h3'>{product.name}</Typography>
          <Divider sx={{ mb: 2 }} />
          <Typography gutterBottom color='secondary' variant='h4'>
            {formatPrice(product.price)}
          </Typography>
          <TableContainer>
            <Table>
              <TableBody>
                <TableRow>
                  <TableCell>Name</TableCell>
                  <TableCell>{product.name}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>Description</TableCell>
                  <TableCell>{product.description}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>Type</TableCell>
                  <TableCell>{product.productType}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>Brand</TableCell>
                  <TableCell>{product.productBrand}</TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </TableContainer>
          {/* <Grid container spacing={2}>
          <Grid item xs={6}>
            <TextField
              onChange={inputChange}
              variant='outlined'
              type='number'
              label='Quantity in Cart'
              fullWidth
              value={quantity}
            />
          </Grid>
          <Grid item xs={6}>
            <LoadingButton
              sx={{ height: '55px' }}
              color='primary'
              size='large'
              variant='contained'
              fullWidth
              loading={submitting}
              onClick={updateQuantity}
            >
              {item ? 'Update Quantity' : 'Add to Cart'}
            </LoadingButton>
          </Grid>
        </Grid> */}
        </Grid>
      </Grid>
    )
  );
}
