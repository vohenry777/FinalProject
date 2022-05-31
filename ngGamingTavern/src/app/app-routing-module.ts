import { MeetupComponent } from './components/meetup/meetup.component';
import { ServerComponent } from './components/server/server.component';
import { EquipmentComponent } from './components/equipment/equipment.component';
import { LogoutComponent } from "./components/auth/logout/logout.component";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/auth/login/login.component";
import { UsersTableComponent } from "./components/user/users-table/users-table.component";
import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { UserProfileComponent } from "./components/user/profile/user-profile/user-profile.component";
import { GameComponent } from './components/game/game.component';

const routes: Routes = [
  { path: "", pathMatch: "full", component: HomeComponent },
  { path: "user", pathMatch: "full", component: UserProfileComponent },
  { path: "users", pathMatch: "full", component: UsersTableComponent },
  { path: "login", pathMatch: "full", component: LoginComponent },
  { path: "logout", pathMatch: "full", component: LogoutComponent },
  { path: "profile", pathMatch: "full", component: UserProfileComponent },
  { path: "equipment", pathMatch: "full", component: EquipmentComponent},
  { path: "server", pathMatch: "full", component: ServerComponent},
  { path: "meetup", pathMatch: "full", component: MeetupComponent},
  { path: "game", pathMatch: "full", component: GameComponent}
];

@NgModule({
  // imports: [RouterModule.forRoot(routes, {useHash: true})],
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
