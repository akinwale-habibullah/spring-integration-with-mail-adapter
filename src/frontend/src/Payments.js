import React, { useEffect, useState } from 'react'
import {
  Box,
  Skeleton
} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';

const columns = [
  { field: 'id', headerName: 'ID', width: 70 },
  { field: 'amount', headerName: 'Amount', width: 130, type: 'number' },
  { field: 'receiptDate', headerName: 'Receipt Date', width: 130 },
  { field: 'remarks', headerName: 'Remarks', width: 700 }
];

function PaymentsSkeleton() {
  return (
    <>
      <Skeleton variant="rectangular" animation="wave" width={"100%"} height={"80vh"} />
    </>
  )
}

function Payments() {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    async function getTransactions() {
      let response = await fetch("/api/payments");
      return await response.json();
    }

    getTransactions()
      .then(payments => {
        setPayments(payments);
        setLoading(false);
      })
      .catch(err => console.error('Err: ' + err));
  }, [])

  if (loading) {
    return <PaymentsSkeleton />
  }

  return (
    <Box>
      <DataGrid
        columns={columns}
        rows={payments}
        initialState={{
          pagination: {
            paginationModel: { page: 0, pageSize: 20 },
          },
        }}
        pageSizeOptions={[ 5, 10, 15, 20, 50, 100 ]}
        checkboxSelection
      />
    </Box>
  );
}



export default Payments