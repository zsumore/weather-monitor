import React from 'react';
import { Route, Redirect, IndexRoute } from 'react-router';

// Here we define all our material-ui ReactComponents.
//AtmosphericElectric shorthand ae
import Master from './components/master';
import AEHourPage from './components/pages/ae/hour-page';
import AEDatePage from './components/pages/ae/date-page';
import AENowPage from './components/pages/ae/now-page';
import AEMonthPage from './components/pages/ae/month-page';


/**
 * Routes: https://github.com/rackt/react-router/blob/master/docs/api/components/Route.md
 *
 * Routes are used to declare your view hierarchy.
 *
 * Say you go to http://material-ui.com/#/components/paper
 * The react router will search for a route named 'paper' and will recursively render its
 * handler and its parent handler like so: Paper > Components > Master
 */
const AppRoutes = (
<Route path = '/' component = {Master} >
    <IndexRoute component={AENowPage} />
    <Redirect from='ae' to='/ae/now' />
    <Route path = 'ae' >
    <Route path='now' component={AENowPage} />
    <Route path='hour' component={AEHourPage} />
    <Route path='hour/:datetime' component={AEHourPage} />
    <Route path='date' component = {AEDatePage}/>
    <Route path='month' component = {AEMonthPage}/>
    </Route> 
    </Route>
);

export default AppRoutes;
