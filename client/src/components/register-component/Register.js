import { Form, Input, Button, Checkbox, Row ,Col,message,notification,Card} from 'antd';
import { UserOutlined, LockOutlined,MailOutlined} from '@ant-design/icons';
import "./Register.css"
import { Link, useHistory } from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import {CleanupErrors, RegisterUser} from "../../store/actions/Action";
import { useEffect } from 'react';
import { useThemeSwitcher } from "react-css-theme-switcher";
import Theme from '../theme-switch/Theme';




function Register(props) {
  const dispatch = useDispatch();
  const {currentTheme}=useThemeSwitcher();
  const history=useHistory();
  const auth=useSelector(state=>state.auth)
  const error=useSelector(state=>state.error)
  
  useEffect(() => {
    if(auth.isAuthenticated){
      history.push("/dashboard");
     }
   
  }, [auth.isAuthenticated, history])
  
  
  if(auth.isLoading && error.isLoading){
    message.loading({duration:2,content:"Registering User.."})
       
   }
   if(error.hasErrors && error.isLoading===false){
    notification.error({
      message: 'Registration Error',
      description:error.errorHandler.message,
    });
    dispatch(CleanupErrors())

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
          layout={currentTheme==="dark"?"horizontal":"vertical"}
          name="normal_registration"
          className="registration-form"
          initialValues={{ remember: true }}
          onFinish={onFinish}
        >
            <Form.Item
            label={currentTheme==="dark"?"":"UserName"}
            name="username"
            rules={[{ required: true,min:6, message: "UserName must be minimum 6 characters."}]}
          >
            <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="UserName" />
          </Form.Item>
          <Form.Item
          label={currentTheme==="dark"?"":"Email"}
            name="email"
            rules={[{ required: true,type: "email", message: "The input is not valid E-mail!"}]}
          >
            <Input prefix={<MailOutlined  className="site-form-item-icon" />} placeholder="Email" />
          </Form.Item>
          <Form.Item
          label={currentTheme==="dark"?"":"Password"}
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
          label={currentTheme==="dark"?"":"Confirm Password"}
      
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
          <div>
            <Theme/>
          </div>
          
            <div className={currentTheme==="dark"?"basic-form":"light-form"}>
            <Row justify='center' align='center'>
        <div className="site-statistic-demo-card">
        <Col span={12}>
        <Card className="intro-card"
        style={{ width: 350 }}>
    <p className="card-header">Register Below</p>
      
                <RegisterForm/>
  </Card>
        </Col>
        </div>
</Row>
               
                
            </div>
            
        </div>
    )
}
export default Register;