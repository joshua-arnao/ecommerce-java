import { useEffect, useState } from 'react';
import type { Product } from '../../app/models/product';
import ProductList from './ProductList';
import agent from '../../app/api/agent';
import Spinner from '../../app/layout/Spinner';
import {
  Box,
  FormControl,
  FormControlLabel,
  FormLabel,
  Grid,
  Pagination,
  Paper,
  Radio,
  RadioGroup,
  TextField,
  Typography,
} from '@mui/material';
import type { Brand } from '../../app/models/brand';
import type { Type } from '../../app/models/type';

const sortOptions = [
  { value: 'asc', label: 'Ascending' },
  { value: 'desc', label: 'Descending' },
];

export default function Catalog() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [brands, setBrands] = useState<Brand[]>([]);
  const [types, setTypes] = useState<Type[]>([]);
  const [selectedSort, setSelectedSort] = useState('asc');
  const [selectedBrand, setSelectedBrand] = useState('All');
  const [selectedType, setSelectedType] = useState('All');
  const [selectedBrandId, setSelectedBrandId] = useState(0);
  const [selectedTypeId, setSelectedTypeId] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;

  //fetch('http://localhost:8081/api/products')
  //     .then((response) => response.json())
  //     .then((data) => setProducts(data.content));
  // }, []);

  // useEffect(() => {
  //   agent.Store.list()
  //     .then((products) => setProducts(products.content))
  //     .catch((error) => console.error(error))
  //     .finally(() => setLoading(false));
  // }, []);

  useEffect(() => {
    Promise.all([
      agent.Store.list(currentPage, pageSize),
      agent.Store.brands(),
      agent.Store.types(),
    ])
      .then(([productsRes, brandsResp, typesRes]) => {
        setProducts(productsRes.content);
        setTotalItems(productsRes.totalElements);
        setBrands(brandsResp);
        setTypes(typesRes);
      })
      .catch((error) => console.error(error))
      .finally(() => setLoading(false));
  }, [currentPage, pageSize]);

  const loadProducts = (selectedSort, searchKeyword = '') => {
    setLoading(true);
    let brandId = selectedBrandId != 0 ? selectedBrandId : undefined;
    let typeId = selectedTypeId != 0 ? selectedTypeId : undefined;
    const sort = 'name';
    const order = selectedSort === 'desc' ? 'desc' : 'asc';

    let url = `${agent.Store.apiUrl}?sort=${sort}&order=${order}`;
    if (brandId !== undefined || typeId !== undefined) {
      url += '&';
      if (brandId !== undefined) url += `brandId=${brandId}&`;
      if (typeId !== undefined) url += `typeId=${typeId}&`;

      url = url.replace(/&$/, '');
    }
    if (searchKeyword) {
      console.log(searchKeyword);
      agent.Store.search(searchKeyword)
        .then((productsRes) => {
          setProducts(productsRes.content);
          setTotalItems(productsRes.length);
        })
        .catch((error) => console.error(error))
        .finally(() => setLoading(false));
    } else {
      agent.Store.list(pageSize, pageSize, undefined, undefined, url)
        .then((productsRes) => {
          setProducts(productsRes.content);
          setTotalItems(productsRes.totalElements);
        })
        .catch((error) => console.error(error))
        .finally(() => setLoading(false));
    }
  };

  useEffect(() => {
    loadProducts(selectedSort);
  }, [selectedBrandId, selectedTypeId]);

  const handleSortChange = (event: any) => {
    const selectedSort = event.target.value;
    setSelectedSort(selectedSort);
    loadProducts(selectedSort);
  };
  const handleBrandChange = (event: any) => {
    const selectedBrand = event.target.value;
    const brand = brands.find((b) => b.name === selectedBrand);
    setSelectedBrand(selectedBrand);
    if (brand) {
      setSelectedBrandId(brand.id);
      loadProducts(selectedSort, searchTerm);
    }
  };
  const handleTypeChange = (event: any) => {
    const selectedType = event.target.value;
    const type = types.find((t) => t.name === selectedType);
    setSelectedType(selectedType);
    if (type) {
      setSelectedTypeId(type.id);
      loadProducts(selectedSort, searchTerm);
    }
  };

  const handlePageChange = (event, page) => {
    setCurrentPage(page);
  };

  if (loading) return <Spinner message='Loading Products...' />;
  return (
    <Grid container spacing={4}>
      <Grid size={{ xs: 12 }}>
        <Box mb={2} textAlign='center'>
          <Typography variant='subtitle1'>
            Displaying {(currentPage - 1) * pageSize + 1}-
            {Math.min(currentPage * pageSize, totalItems)} of {totalItems} items
          </Typography>
        </Box>
        <Box mt={4} display='flex' justifyContent='center'>
          <Pagination
            count={Math.ceil(totalItems / pageSize)}
            color='primary'
            onChange={handlePageChange}
            page={currentPage}
          />
        </Box>
      </Grid>
      <Grid size={{ xs: 3 }}>
        <Paper sx={{ mb: 2 }}>
          <TextField
            label='Search products'
            variant='outlined'
            fullWidth
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                // Trigger search action
                loadProducts(selectedSort, searchTerm); // Pass the search term to loadProducts
              }
            }}
          />
        </Paper>
        <Paper sx={{ mb: 2, p: 2 }}>
          <FormControl>
            <FormLabel id='sort-by-name-label'>Sort by Name</FormLabel>
            <RadioGroup
              aria-label='sort-by-name'
              name='sort-by-name'
              value={selectedSort}
              onChange={handleSortChange}
            >
              {sortOptions.map(({ value, label }) => (
                <FormControlLabel
                  key={value}
                  value={value}
                  control={<Radio />}
                  label={label}
                />
              ))}
            </RadioGroup>
          </FormControl>
        </Paper>
        <Paper sx={{ mb: 2, p: 2 }}>
          <FormControl>
            <FormLabel id='brands-label'>Brands</FormLabel>
            <RadioGroup
              aria-label='brands'
              name='brands'
              value={selectedBrand}
              onChange={handleBrandChange}
            >
              {brands.map((brand) => (
                <FormControlLabel
                  key={brand.id}
                  value={brand.name}
                  control={<Radio />}
                  label={brand.name}
                />
              ))}
            </RadioGroup>
          </FormControl>
        </Paper>
        <Paper sx={{ mb: 2, p: 2 }}>
          <FormControl>
            <FormLabel id='types-label'>Types</FormLabel>
            <RadioGroup
              aria-label='types'
              name='types'
              value={selectedType}
              onChange={handleTypeChange}
            >
              {types.map((type) => (
                <FormControlLabel
                  key={type.id}
                  value={type.name}
                  control={<Radio />}
                  label={type.name}
                />
              ))}
            </RadioGroup>
          </FormControl>
        </Paper>
      </Grid>
      <Grid size={{ xs: 9 }}>
        <ProductList products={products} />
      </Grid>
      <Grid size={{ xs: 12 }}>
        <Box mt={4} display='flex' justifyContent='center'>
          <Pagination
            count={Math.ceil(totalItems / pageSize)}
            color='primary'
            onChange={handlePageChange}
            page={currentPage}
          />
        </Box>
      </Grid>
    </Grid>
  );
}
