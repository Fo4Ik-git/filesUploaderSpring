package com.fo4ik.webFilesSaver.controllers;

import com.fo4ik.webFilesSaver.config.Config;
import com.fo4ik.webFilesSaver.model.FileModel;
import com.fo4ik.webFilesSaver.model.User;
import com.fo4ik.webFilesSaver.repo.FileRepo;
import com.fo4ik.webFilesSaver.repo.LogoRepo;
import com.fo4ik.webFilesSaver.repo.UserRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class FileController {

    private final UserRepo userRepo;
    private final LogoRepo logoRepo;
    private final FileRepo fileRepo;

    String currentPath;

    public FileController(UserRepo userRepo, LogoRepo logoRepo, FileRepo fileRepo) {
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;
        this.fileRepo = fileRepo;
    }

    @GetMapping("/files")
    public String getFiles(@AuthenticationPrincipal User user, Model model) {
        Config config = new Config(userRepo, logoRepo);
        config.getUserLogo(user, model);
        try {
            currentPath = "files/users/" + user.getId() + "/files/";
            showFiles(currentPath, user, model);

        } catch (Exception e) {
            model.addAttribute("files", e.getMessage());
        }


        return "files";
    }

    private void showFiles(String path, User user, Model model) {
        List<FileModel> files = fileRepo.findAllByUserId(user.getId());
        model.addAttribute("files", files);
    }

    @PostMapping("/files/uploadFile")
    public String uploadFile(@AuthenticationPrincipal User user, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) {
        try {
            Iterable<FileModel> filesList = fileRepo.findAllByUserId(user.getId());
            System.out.println("FileController 62");
            if (!fileUpload.isEmpty()) {
                for (FileModel file : filesList) {
                    System.out.println("FileController 65");
                    if (file.getName().equals(fileUpload.getOriginalFilename())) {
                        model.addAttribute("message", "File already exists, change name of file");
                        return "files";
                    }
                }
                System.out.println("FileController 70");
                FileModel file = new FileModel(fileUpload.getOriginalFilename(), saveFile(user, fileUpload, model), false, user);
                fileRepo.save(file);
            } else {
                model.addAttribute("message", "File is empty");
                return "files";
            }


            /*if (!fileUpload.isEmpty()) {

                for (FileModel file : filesList) {
                    if (file.getFileName().equals(fileUpload.getOriginalFilename())) {
                        model.addAttribute("message", "File already exists, change name of file");
                    } else {
                        file.setName(fileUpload.getOriginalFilename());
                        file.setPath(saveFile(user, fileUpload, model));
                        fileRepo.save(file);
                    }
                }
            } else {
                model.addAttribute("message", "File is empty");
            }*/

            /*if (files != null && !fileUpload.getOriginalFilename().equals("")) {
                if (fileUpload.getOriginalFilename().equals(files.getFileName())) {
                    //model.addAttribute("message", "File already exist");

                } else {
                    files.setName(fileUpload.getOriginalFilename());
                    files.setPath(saveFile(user, fileUpload, model));
                    fileRepo.save(files);
                }
            } else {
                FileModel file = new FileModel(fileUpload.getOriginalFilename(), saveFile(user, fileUpload, model), false, user);
                fileRepo.save(file);
                System.out.println("FileController 76: " + file);
            }*/
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/files";
        }
        return "redirect:/files";
    }

    private String saveFile(User user, MultipartFile fileUpload, Model model) {
        try {
            Path folder = Path.of(createUserFolder(user.getId()) + "/");
            Path path = Path.of(folder + "/" + fileUpload.getOriginalFilename());
            byte[] bytes = fileUpload.getBytes();
            Files.write(path, bytes);

            return String.valueOf(path);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return null;
    }

    private Path createUserFolder(Long userId) {
        File file = new File("files/users/" + userId + "/files");
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            return Path.of(file.getPath());
        } catch (Exception e) {
        }
        return null;
    }

    @PostMapping("/files/deleteFile")
    public String deleteFile(@AuthenticationPrincipal User user, String filePath, Model model) {
        System.out.println("FileController 117: " + filePath);
        try {
            String tmpPath = currentPath + filePath;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
                FileModel fileModel = fileRepo.findByPath(filePath);
                fileRepo.delete(fileModel);
            }

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/files";
    }

    @PostMapping("/files/createFolder")
    public String createFolder(@AuthenticationPrincipal User user, @RequestParam("folderName") String folderName, Model model) {
        try {
            String tmpPath = currentPath + folderName;
            File file = new File(tmpPath);
            if (!file.exists()) {
                file.mkdirs();
                FileModel fileModel = new FileModel(folderName, tmpPath, true, user);
                fileRepo.save(fileModel);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/files";
    }

}
