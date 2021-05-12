import { Provider } from 'react-redux';
import { Route, BrowserRouter as Router } from 'react-router-dom';
import Landing from './components/landing-component/Landing';
import store from './store/store';
import Login from './components/login-component/Login';
import Register from './components/register-component/Register';
import Privacy from './components/privacy-component/Privacy';
import Activate from './components/account-activation-component/Activate';




function App() {
  
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Route exact path="/"component={Landing}/>
          <Route exact path="/login" component={Login} />
          <Route exact path="/register" component={Register}/>
          <Route exact path="/privacy-policy" component={Privacy}/>
          <Route exact path="/token/:tokenid"component={Activate}/>
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
