import { Provider} from 'react-redux';
import { Route, BrowserRouter as Router } from 'react-router-dom';
import Landing from './components/landing-component/Landing';
import store from './store/store';
import Login from './components/login-component/Login';
import Register from './components/register-component/Register';
import Privacy from './components/privacy-component/Privacy';
import Activate from './components/account-activation-component/Activate';
import SetAuthToken from './utils/SetAuthHeader';
import jwt_decode from 'jwt-decode';
import { LOGIN_USER} from './store/actions/actionTypes';
import Oauth2 from './components/oauth2-component-handler/Oauth2';
import Dashboard from './components/dashboard-component/Dashboard';
import PollPageHeader from './components/page-header-component/PageHeader';
import { LogOutUser } from './store/actions/Action';
import CreatePollDashboard from './components/create-poll-dashboard/CreatePollDashboard';





//const redux_store=useStore();

//SESSION MANAGEMENT IN MAIN APP
if(localStorage.jwtToken){
  const token=localStorage.jwtToken
  //set header
  SetAuthToken(token)
  //decode
  const decoded=jwt_decode(token)
  //call store to keep user authenticated and in session
  store.dispatch({
    type:LOGIN_USER,
    payload:decoded
  })
  
  
   // Check for expired token
   const currentTime= Date.now()/1000;
   if (decoded.exp < currentTime) {
     //set header
    SetAuthToken(false)
    //remove token
    localStorage.removeItem("jwtToken");
     // Logout user
    window.location.href="/"
    store.dispatch(LogOutUser())
   }
  }
function App() {
  
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <PollPageHeader/>
          <Route exact path="/"component={Landing}/>
          <Route exact path="/login" component={Login} />
          <Route exact path="/register" component={Register}/>
          <Route exact path="/privacy-policy" component={Privacy}/>
          <Route exact path="/activate-account/:tokenid"component={Activate}/>
          <Route exact path="/oauth2/redirect" component={Oauth2}/>
          <Route exact path="/dashboard" component={Dashboard}/>
          <Route exact path="/create_poll" component={CreatePollDashboard}/>
          <Route  path='/issues' component={() => { 
            window.location.href = 'https://github.com/wambugucoder/FINAL-YEAR-PROJECT/issues/new';  
            return null;
            }}/>
            <Route  path='/project' component={() => { 
            window.location.href = 'https://github.com/wambugucoder/FINAL-YEAR-PROJECT';  
            return null;
            }}/>
        </div>
      </Router>

    </Provider>
    
  );
}

export default App;
