import React, { useEffect, useState } from "react";
import {
  Button,
  Cascader,
  DatePicker,
  Form,
  Input,
  InputNumber,
  message,
} from "antd";
import api from "../../../../config/axios";

const { RangePicker } = DatePicker;

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 10 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 20 },
  },
};

const handleChange = (value) => {
  console.log(value);
};

const PromoCreateForm = () => {
  const [options, setOptions] = useState([]);

  useEffect(() => {
    const fetchCategoriesAndProducts = async () => {
      try {
        const [categoriesResponse, productsResponse] = await Promise.all([
          api.get("api/category"),
          api.get("api/productSell"),
        ]);

        const categories = categoriesResponse.data;
        const products = productsResponse.data;
        console.log(categories);
        console.log(products);
        const newOptions = categories.map((category) => ({
          label: category.name,
          value: category.id,
          key: `category-${category.id}`, // Ensure unique keys for categories
          children: products
            .filter((product) => product.category_id === category.id)
            .map((product) => ({
              label: product.pname,
              value: product.productID,
              key: `product-${product.productID}`, // Ensure unique keys for products
            })),
        }));

        setOptions(newOptions);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchCategoriesAndProducts();
  }, []);

  const onFinish = async (values) => {
    try {
      const [startDate, endDate] = values.RangePicker;
      const productSell_IDs = values.Product.map((item) => item[1]);

      const payload = {
        code: values.Name,
        description: values.description,
        startDate: startDate.format("YYYY-MM-DD"),
        endDate: endDate.format("YYYY-MM-DD"),
        discount: values.percentage,
        productSell_IDs,
      };

      await api.post("/api/promotion/create", payload);
      message.success("Promotion created successfully!");
    } catch (error) {
      console.error("Error creating promotion:", error);
      message.error("Failed to create promotion.");
    }
  };

  return (
    <Form
      {...formItemLayout}
      style={{
        maxWidth: 600,
      }}
      onFinish={onFinish}
    >
      <Form.Item
        label="Tên"
        name="Name"
        rules={[
          {
            required: true,
            message: "Please input!",
          },
        ]}
      >
        <Input.TextArea />
      </Form.Item>

      <Form.Item
        label="Phần trăm khuyến mãi"
        name="percentage"
        rules={[
          {
            required: true,
            message: "Please input!",
          },
        ]}
      >
        <InputNumber
          style={{
            width: "100%",
          }}
        />
      </Form.Item>

      <Form.Item
        label="Mô tả"
        name="description"
        rules={[
          {
            required: true,
            message: "Please input!",
          },
        ]}
      >
        <Input.TextArea />
      </Form.Item>

      <Form.Item
        label="Ngày bắt đầu - Ngày kết thúc"
        name="RangePicker"
        rules={[
          {
            required: true,
            message: "Please input!",
          },
        ]}
      >
        <RangePicker />
      </Form.Item>

      <Form.Item label="Sản phẩm" name="Product">
        <Cascader
          style={{
            width: "100%",
          }}
          options={options}
          onChange={handleChange}
          multiple
          maxTagCount="responsive"
        />
      </Form.Item>

      <Form.Item
        wrapperCol={{
          offset: 7,
          span: 30,
        }}
      >
        <Button type="primary" htmlType="submit">
          Submit
        </Button>
      </Form.Item>
    </Form>
  );
};

export default PromoCreateForm;
