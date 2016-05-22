var slices = 8,
  angle = 360 / slices,
  sphere = document.querySelector('.sphere');
for (var i = 0; i < (slices - 1); i++) {
  var slice = document.createElement('div');
  slice.className = 'circle';
  slice.style.transform = 'rotateY(' + angle * i + 'deg)';
  sphere.appendChild(slice);
}

var stars = 100,
  vh = window.innerHeight,
  vw = window.innerWidth;

function randomStar(selector) {
  var star = document.createElement('div');
  star.className = 'star';
  star.style.left = Math.floor((Math.random() * vw * 2) - 20) + 'px';
  star.style.top = Math.floor(Math.random() * (vh - 40) + 20) + 'px';
  document.querySelector(selector).appendChild(star);
}
for (var i = 0; i < stars; i++) {
  randomStar('.layer1');
  randomStar('.layer2');
  randomStar('.layer3');
}