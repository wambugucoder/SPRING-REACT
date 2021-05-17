import { Form, Input, Button, Checkbox, Row ,Col,message,notification} from 'antd';
import { UserOutlined, LockOutlined,MailOutlined} from '@ant-design/icons';
import "./Register.css"
import { Link, useHistory } from 'react-router-dom';
import Title from 'antd/lib/typography/Title';
import {useDispatch, useSelector} from 'react-redux';
import {RegisterUser} from "../../store/actions/Action";
import { useEffect } from 'react';




function Register(props) {
  const dispatch = useDispatch();
  const history=useHistory();
  const auth=useSelector(state=>state.auth)
  const error=useSelector(state=>state.error)
  
  useEffect(() => {
    if(auth.isAuthenticated){
      history.push("/dashboard");
     }
   
  }, [auth.isAuthenticated])
  
  
  if(auth.isLoading && error.isLoading){
    message.loading("Registering User..")
       
   }
   if(error.hasErrors && error.isLoading===false){
    notification.error({
      message: 'Registration Error',
      description:error.errorHandler.message,
    });



   }
   if(auth.isRegistered){
      history.push("/login")

   }
   
    
   const onFinish = (values) => {
        console.log(values)
       const UserData={
           username:values.username,
           email:values.email,
           password:values.password
       }
        dispatch(RegisterUser(UserData));
      }

    const RegisterForm=()=>{
        return(
          
          <Form
          name="normal_registration"
          className="registration-form"
          initialValues={{ remember: true }}
          onFinish={onFinish}
        >
            <Form.Item
            name="username"
            rules={[{ required: true,min:6, message: "UserName must be minimum 6 characters."}]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="UserName" />
          </Form.Item>
          <Form.Item
            name="email"
            rules={[{ required: true,type: "email", message: "The input is not valid E-mail!"}]}
          >
            <Input prefix={<MailOutlined  className="site-form-item-icon" />} placeholder="Email" />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required:true,min: 6, message: 'Password must be minimum 6 characters.'}]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Password"
            />
          </Form.Item>
          <Form.Item
            name="confirm"
            dependencies={['password']}
            rules={[
                {
                  required: true,
                  message: 'Please confirm your password!',
                },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue('password') === value) {
                      return Promise.resolve();
                    }
      
                    return Promise.reject(new Error('The two passwords that you entered do not match!'));
                  },
                }),
              ]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Confirm Password"
            />
          </Form.Item>
          <Form.Item
        name="agreement"
        valuePropName="checked"
        rules={[
          {
            validator: (_, value) =>
              value ? Promise.resolve() : Promise.reject(new Error('Should accept agreement')),
          },
        ]}
        
      >
        <Checkbox>
          I have read the <a href="/privacy-policy">agreement</a>
        </Checkbox>
      </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" className="login-form-button">
              Register
            </Button>
            Or <Link to="/login">Login now!</Link>
          </Form.Item>
        </Form>
        );
      }
    return (
        <div className="register-form">
            <div className="registration-title">
                <Title level={2}>Register Below </Title>
            </div>
            <div className="basic-form">
                <Row justify="center" align="center">
                <Col xs={{ span: 14, offset: 1 }} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 6, offset: 1 }}>
                <RegisterForm/>
                </Col>
                </Row>
            </div>
            
        </div>
    )
}
export default Register;