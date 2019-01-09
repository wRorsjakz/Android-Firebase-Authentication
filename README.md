# Android-Firebase-Authentication

_A fully functional Android authentication application, built with Firebase_

<img src="https://img.shields.io/badge/minSdkVersion-21-red.svg?style=true" alt="minSdkVersion 21" data-canonical-src="https://img.shields.io/badge/minSdkVersion-24-red.svg?style=true" style="max-width:100%;"> <img src=https://img.shields.io/badge/compileSdkVersion-28-brightgreen.svg alt="compileSdkVersion 28" data-canonical-src="https://img.shields.io/badge/compileSdkVersion-27-yellow.svg?style=true" style="max-width:100%;">

<br>
<br>

<img src="https://firebase.google.com/downloads/brand-guidelines/PNG/logo-built_black.png" width="50%">

<br>

## Screenshots

<img src="https://user-images.githubusercontent.com/39665412/50907786-28c7a080-1463-11e9-9cf9-6234c8954407.png" width="50%">
<img src="https://user-images.githubusercontent.com/39665412/50907787-28c7a080-1463-11e9-8398-97380e3216d7.png" width="50%">
<img src="https://user-images.githubusercontent.com/39665412/50907783-282f0a00-1463-11e9-9c40-972aa30264cb.png" width="50%">


## Features

* User Registration
    * Validates email address entry pattern using `android.util`
    * Validate password entry meets decleared requirements using `java.util.regex`
    * Send email verification
    
* Email Sign In
    * Checks if email address has been verified
    * Firebase authentication state persistance

* Google Sign In
    * Add and remove Google account linked to app
    * Firebase authentication state persistance

* Password reset through email

* Log Out

## Credits

 * Firebase assets download [here](https://firebase.google.com/brand-guidelines/)
 * [GrenderG/Toasty](https://github.com/GrenderG/Toasty) - The usual Toast, but with steroidss

## License

```tex
MIT License

Copyright (c) 2018 Nicholas Gan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```