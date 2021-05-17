import { Form, Input, Button, Checkbox, Row ,Col, Alert,message,notification} from 'antd';
import { UserOutlined, LockOutlined,GoogleOutlined,GithubFilled } from '@ant-design/icons';
import "./Login.css"
import { Link, useHistory } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { LoginUser } from '../../store/actions/Action';
import { GITHUB_AUTH_URL, GOOGLE_AUTH_URL } from '../../constants/Constant';
import { useEffect } from 'react';





function Login(props) {

  const auth=useSelector(state=>state.auth);
  const error=useSelector(state=>state.error);
  const dispatch=useDispatch();
  const history=useHistory();

  useEffect(() => {
    if(auth.isAuthenticated){
      history.push("/dashboard");
     }
   
  }, [auth.isAuthenticated])
 
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
    message.loading("Logging User In..")
       
   }
   if(error.hasLoginErrors && error.isLoading===false){
    notification.error({
      message: 'Login Error',
      description:error.errorHandler.message,
    });
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
        message="Info"
        description="Please Check Your Email To Activate Your Account"
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
      name="normal_login"
      className="login-form"
      initialValues={{ remember: true }}
      onFinish={onFinish}
    >
      <Form.Item
        name="email"
        rules={[{ required: true,type: "email", message: "The input is not valid E-mail!"}]}
      >
        <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Email" />
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
      <Form.Item>
        <Form.Item name="remember" valuePropName="checked" noStyle>
          <Checkbox>Remember me</Checkbox>
        </Form.Item>

        <a className="login-form-forgot" href="">
          Forgot password
        </a>
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

const OnGithubClick=()=>{
  window.location.href=GITHUB_AUTH_URL
}
const OnGoogleClick=()=>{
  window.location.href=GOOGLE_AUTH_URL
}
const GithubOauth=()=>{
  return(
    <Button  className="oauth-redirect-buttons" icon={<GithubFilled/>}
    onClick={OnGithubClick} >Login with Github</Button>
  );
}
const GoogleOauth=()=>{
  return(
<Button  className="oauth-redirect-buttons" icon={<GoogleOutlined/>}onClick={OnGoogleClick}>Login with Google</Button>
  );
}

    
        return (
          <div className="login-page">
            <div className="alert-registration">
            <Row justify="center" align="center">
              <Col xs={{ span: 19, offset: 2 }} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 10, offset: 1 }}>
              <AlertUser/>
              </Col>
              </Row>
            </div>
            <div className="oauth-github-button">
              <Row justify="center" align="center">
              <Col xs={{ span: 12, offset: 2 }} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 6, offset: 1 }}>
                <GithubOauth/>
              </Col>
              </Row>
              </div>
              <div className="oauth-google-button">
              <Row justify="center" align="center">
              <Col xs={{ span: 12, offset: 2}} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 6, offset: 1 }}>
                <GoogleOauth/>
              </Col>
              </Row>
              </div>

            <div className="normal-login">
            <Row justify="center" align="center">
              <Col xs={{ span: 12, offset: 1 }} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 6, offset: 1 }}>
                <LoginForm/>
              </Col>
              </Row>
            </div>

          </div>
           
          );
        
 
};
export default Login;