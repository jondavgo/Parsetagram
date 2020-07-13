# Project 4 - *Parsetagram*

**Parsetagram** is a photo sharing app using Parse as its backend.

Time spent: **22** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] User sees app icon in home screen.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in and log out of his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [X] Style the login page to look like the real Instagram login page.
- [X] Style the feed to look like the real Instagram feed.
- [ ] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
- [X] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [X] Show the username and creation time for each post
- [X] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [X] Allow the logged in user to add a profile photo
  - [X] Display the profile photo with each post
  - [X] Tapping on a post's username or profile photo goes to that user's profile page
  - [X] User Profile shows posts in a grid view
- [X] User can comment on a post and see all comments for each post in the post details screen.
- [X] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [ ] List anything else that you can get done to improve the app functionality!

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. Better implementation of likes (currently I use an array of users)
2. Have both the grid and the linear layouts inside of the profile view like instagram has

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/jondavgo/Parsetagram/blob/master/Persistence.gif' title='Persistence and Home Logo' width='' alt='Persistence and Home Logo' />

<img src='https://github.com/jondavgo/Parsetagram/blob/master/SignInLogIn.gif' title='Sign in, Sign out, and Log in' width='' alt='Sign in, Sign out, and Log in' />

<img src='https://github.com/jondavgo/Parsetagram/blob/master/Post%2C%20Comment%2C%20Like.gif' title='Post,Comment,and Likes' width='' alt='Post,Comment,and Likes' />

<img src='https://github.com/jondavgo/Parsetagram/blob/master/SwipeRefresh.gif' title='SwipeToRefresh' width='' alt='SwipeToRefresh' />

<img src='https://github.com/jondavgo/Parsetagram/blob/master/PfpChange.gif' title='Profile Pic Changing' width='' alt='Profile Picture Changing' />

GIF created with [Kap](http://getkap.co).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes
This app was by far my favorite to create. I felt like the stretch goals were a considerable challenge, but not to the point that I could not overcome them. 
For comments, for example, I had to use my knowledge I gained from implementing all three of the fragments to make it possible.

## License

    Copyright [2020] [Jonathan Gomez]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
