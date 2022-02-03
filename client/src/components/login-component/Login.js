import { Form, Input,Card ,Button, Checkbox, Row ,Col, Alert,message,notification} from 'antd';
import { UserOutlined, LockOutlined} from '@ant-design/icons';
import "./Login.css"
import { useThemeSwitcher } from "react-css-theme-switcher";
import { Link, useHistory } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { CleanupErrors, LoginUser } from '../../store/actions/Action';
import { useEffect } from 'react';
import Theme from '../theme-switch/Theme';






function Login(props) {

  const auth=useSelector(state=>state.auth);
  const error=useSelector(state=>state.error);
  const { currentTheme } = useThemeSwitcher();
  const dispatch=useDispatch();
  const history=useHistory();

  useEffect(() => {
    if(auth.isAuthenticated){
      history.push("/dashboard");
     }
   
  }, [auth.isAuthenticated,history])
 
  useEffect(() => {
    if(props.location.state && props.location.state.error) {
      notification.error({
        message: 'Oauth Error',
        description:props.location.state.error,
      });
      props.history.replace({
        pathname:props.location.pathname,
          state: {}
       });
  }
  })

  if(auth.isLoading && error.isLoading){
    message.loading({duration:2,content:"Logging User In.."})
       
   }
   if(error.hasLoginErrors && error.isLoading===false){
    notification.error({
      message: 'Login Error',
      description:error.errorHandler.message,
    });
    dispatch(CleanupErrors())
  }
  


  const onFinish = (values) => {
    const UserData={
      email:values.email,
      password:values.password
    }
    dispatch(LoginUser(UserData));
  }

  const AlertUser=()=>{
    if(auth.isRegistered){
      return(
        <Alert
        message="Account Activation"
        description="Please Check Your Email or Spam Folder To Activate Your Account."
        type="info"
        showIcon
        closable
      />
      );
    }
    else{
      return(
        <div></div>
      );
     
    }

  }

  const LoginForm=()=>{
    return(
      <Form
      layout={currentTheme==="dark"?"horizontal":"vertical"}
      name="normal_login"
      className="login-form"
      initialValues={{ remember: true }}
      onFinish={onFinish}
    >
      <Form.Item
       label={currentTheme==="dark"?"":"Email"}
        name="email"
        rules={[{ required: true,type: "email", message: "The input is not valid E-mail!"}]}
      >
       <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Email" />
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
          name="remember" valuePropName="checked" >
          <Checkbox>Remember me</Checkbox>
        </Form.Item>
      

      <Form.Item>
        <Button type="primary" htmlType="submit" className="login-form-button">
          Log in
        </Button>
        Or <Link to="/register">register now!</Link>
      </Form.Item>
    </Form>
    );
  }



    
        return (
          <div className="login-page">
            <div>
              <Theme />
            </div>
            <div className="alert-registration">
            <Row justify="center" align="center">
              <Col xs={{ span: 19, offset: 2 }} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 10, offset: 1 }}>
              <AlertUser/>
              </Col>
              </Row>
            </div>

            <div >
            <Row justify='center' align='center'>
        <div className="site-statistic-demo-card">
        <Col span={12}>
        <Card className="intro-card"
        style={{ width: 350 }}
  >
    <p className="card-header">Sign In To Your Account</p>
    <p>By Logging In you agree to our <Link to="/privacy-policy">Terms and Conditions.</Link></p>
                <LoginForm/>
              
  </Card>
        </Col>
        </div>
</Row>
           
            </div>

          </div>
           
          );
        
 
};
export default Login;
