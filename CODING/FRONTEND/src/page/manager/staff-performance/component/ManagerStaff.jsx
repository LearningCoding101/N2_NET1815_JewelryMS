import React, { useEffect, useState } from "react";
import {
  Button,
  DatePicker,
  Form,
  Input,
  Modal,
  Popconfirm,
  Spin,
  Table,
  notification,
} from "antd";
import api from "../../../../config/axios";
import { useForm } from "antd/es/form/Form";
import dayjs from "dayjs";
import ShiftChart from "./ShiftChart"; // Import ShiftChart component

function ManagerStaff() {
  const [dataSource, setDataSource] = useState([]);
  const [visible, setVisible] = useState(0); // 0: closed, 1: create, 2: update
  const [oldData, setOldData] = useState({});
  const [formVariable] = useForm();
  const [loading, setLoading] = useState(true);
  const [shiftModalVisible, setShiftModalVisible] = useState(false);
  const [selectedShiftData, setSelectedShiftData] = useState([]);

  const columns = [
    {
      title: "Tên",
      dataIndex: "username",
      key: "username",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Số điện thoại",
      dataIndex: "phoneNumber",
      key: "phoneNumber",
    },
    {
      title: "Lương",
      dataIndex: "salary",
      key: "salary",
      render: (salary) =>
        salary.toLocaleString("vi-VN", { style: "currency", currency: "VND" }),
    },
    {
      title: "Ngày bắt đầu làm việc",
      dataIndex: "startDate",
      key: "startDate",
      render: (startDate) => dayjs(startDate).format("YYYY-MM-DD"),
    },
    {
      title: "Cập nhật",
      dataIndex: "staffID",
      key: "staffID",
      render: (staffID, record) => (
        <Button
          type="primary"
          onClick={() => {
            setVisible(2);
            setOldData({ ...record, startDate: dayjs(record.startDate) });
          }}
        >
          Cập nhật
        </Button>
      ),
    },
    {
      title: "Xóa",
      dataIndex: "staffID",
      key: "staffID",
      render: (staffID) => (
        <Popconfirm
          title="Xóa nhân viên"
          description="Bạn có chắc muốn xóa nhân viên không?"
          onConfirm={() => handleDeleteStaff(staffID)}
          okText="Đồng ý"
          cancelText="Không"
        >
          <Button danger>Xóa</Button>
        </Popconfirm>
      ),
    },
    {
      title: "Ca làm việc",
      dataIndex: "shift",
      key: "shift",
      render: (shift) => (
        <Button
          type="primary"
          onClick={() => {
            setSelectedShiftData(shift);
            setShiftModalVisible(true);
          }}
        >
          Xem ca làm việc
        </Button>
      ),
    },
  ];

  const handleDeleteStaff = async (staffID) => {
    await api.delete(`/api/staff-accounts/${staffID}`);
    const listAfterDelete = dataSource.filter(
      (staff) => staff.staffID !== staffID
    );
    setDataSource(listAfterDelete);
    notification.success({
      message: "Thành công",
      description: "Xóa nhân viên thành công",
    });
  };

  const fetchListStaff = async () => {
    try {
      const response = await api.get("/api/staff-accounts");
      const responseWithStatusTrue = response.data.filter(
        (item) => item.status === 1
      );
      setDataSource(responseWithStatusTrue);
      console.log(responseWithStatusTrue);
      setLoading(false);
    } catch (error) {
      console.error("Không thể lấy dữ liệu nhân viên", error);
      setLoading(true);
    }
  };

  useEffect(() => {
    document.title = "Danh sách nhân viên";
    fetchListStaff();
  }, []);

  useEffect(() => {
    if (visible === 1) {
      formVariable.resetFields();
    } else if (visible === 2) {
      formVariable.setFieldsValue(oldData);
    }
  }, [visible, oldData, formVariable]);

  const handleOpenModal = () => {
    setVisible(1);
    formVariable.resetFields();
  };

  const handleCloseModal = () => {
    setVisible(0);
  };

  const handleOK = () => {
    formVariable.submit();
  };

  const handleFinish = async (values) => {
    values.startDate = dayjs(values.startDate).format("YYYY-MM-DD");
    console.log(values);
    try {
      if (visible === 1) {
        const response = await api.post("/api/account/register", values);
        setDataSource([
          ...dataSource,
          { ...values, staffID: response.data.staffID },
        ]);
      } else if (visible === 2) {
        const response = await api.put(
          `/api/staff-accounts/${oldData.staffID}`,
          {
            phoneNumber: values.phoneNumber,
            salary: values.salary,
            startDate: values.startDate,
            role: "ROLE_STAFF",
            email: values.email,
            username: values.username,
            accountName: values.accountName,
          }
        );
        console.log(response);
        fetchListStaff();
        notification.success({
          message: "Thành công",
          description: "Cập nhật nhân viên thành công",
        });
      }
    } catch (error) {
      console.log(error);
    }
    formVariable.resetFields();
    handleCloseModal();
  };

  return (
    <div className="Manager_StaffAccount">
      {loading ? (
        <Spin size="large" />
      ) : (
        <>
          <Button
            type="primary"
            className="Manager_StaffAccount_Button"
            onClick={handleOpenModal}
          >
            Thêm nhân viên
          </Button>
          <Table dataSource={dataSource} columns={columns} />
        </>
      )}
      <Modal
        title={
          visible === 1 ? "Thêm nhân viên" : "Cập nhật thông tin nhân viên"
        }
        open={visible > 0}
        onCancel={handleCloseModal}
        onOk={handleOK}
      >
        <Form
          form={formVariable}
          labelCol={{ span: 24 }}
          onFinish={handleFinish}
        >
          <Form.Item
            label="Số điện thoại:"
            name="phoneNumber"
            rules={[
              {
                required: true,
                message: "Hãy nhập số điện thoại!",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Email:"
            name="email"
            rules={[
              {
                required: true,
                message: "Hãy nhập email!",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Tên đăng nhập:"
            name="username"
            rules={[
              {
                required: true,
                message: "Hãy nhập tên đăng nhập!",
              },
            ]}
          >
            <Input />
          </Form.Item>
          {visible == 1 ? (
            <Form.Item
              label="Mật khẩu:"
              name="password"
              rules={[
                {
                  required: true,
                  message: "Hãy nhập mật khẩu!",
                },
              ]}
            >
              <Input.Password />
            </Form.Item>
          ) : (
            ""
          )}
          <Form.Item
            label="Ngày bắt đầu làm việc (YYYY-MM-DD):"
            name="startDate"
            rules={[
              {
                required: true,
                message: "Hãy nhập ngày bắt đầu làm việc!",
              },
            ]}
          >
            <DatePicker format="YYYY-MM-DD" />
          </Form.Item>
          <Form.Item
            label="Tên tài khoản:"
            name="accountName"
            rules={[
              {
                required: true,
                message: "Hãy nhập tên tài khoản!",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Lương:"
            name="salary"
            rules={[
              {
                required: true,
                message: "Hãy nhập lương!",
              },
            ]}
          >
            <Input />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Ca làm việc"
        visible={shiftModalVisible}
        onCancel={() => setShiftModalVisible(false)}
        footer={null}
        style={{ width: "600px", height: "600px" }} // Set your desired width and height
      >
        <ShiftChart shiftData={selectedShiftData} />
      </Modal>
    </div>
  );
}

export default ManagerStaff;
