import { lazy } from "react";

// project imports
import MinimalLayout from "layout/MinimalLayout";
import Loadable from "ui-component/Loadable";

// login option 3 routing
const AuthLogin3 = Loadable(lazy(() => import("views/pages/authentication3/Login3")));
const SetName = Loadable(lazy(() => import("views/pages/authentication3/SetName")));

// ==============================|| AUTHENTICATION ROUTING ||============================== //

const AuthenticationRoutes = {
  path: "/",
  element: <MinimalLayout />,
  children: [
    {
      path: "/pages/login/login3",
      element: <AuthLogin3 />
    },
    {
      path: "/setname",
      element: <SetName />
    }
  ]
};

export default AuthenticationRoutes;
