#ifndef UTILS_H
#define UTILS_H

unsigned long getLibraryBase(const char* libName) {

    FILE *fp;
    unsigned long address = 0;

    char filename[32], buffer[1024];
    snprintf(filename, sizeof(filename), "/proc/%d/maps", getpid());
    fp = fopen(filename, "rt");

    if (fp != NULL) {
        while (fgets(buffer, sizeof(buffer), fp)) {
            if (strstr(buffer, libName)) {
                address = (uintptr_t) strtoul(buffer, NULL, 16);
                break;
            }
        }
        fclose(fp);
    }
    return address;
}
unsigned long getRealOffset(unsigned  long libBase, unsigned long address) {
    if ( !libBase )
        return 0;
    return (libBase + address);
}

bool isLibraryLoaded(const char *libraryName) {

    char line[512] = {0};
    FILE *fp = fopen("/proc/self/maps", "rt");
    if (fp != NULL) {
        while (fgets(line, sizeof(line), fp)) {
            if (strstr(line, libraryName)) {
                return true;
            }
        }
        fclose(fp);
    }
    return false;
}

#endif
