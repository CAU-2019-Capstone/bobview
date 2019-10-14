<template>
  <v-footer
    id="core-footer"
    absolute
    height="41"
  >
    <v-spacer />
    <span class="font-weight-light copyright">
      &copy;
      {{ (new Date()).getFullYear() }}
      <a
        href="https://github.com/CAU-2019-Capstone/BobViewFront"
        target="_blank"
      >BobView</a>
    </span>
  </v-footer>
</template>

<script>
  export default {
    data: () => ({
    })
  }
</script>

<style>
#core-footer {
  z-index: 0;
}
</style>
